package es.progcipfpbatoi.todolistspring.model.repositories;

import es.progcipfpbatoi.todolistspring.exceptions.DatabaseErrorException;
import es.progcipfpbatoi.todolistspring.exceptions.DuplicatedTaskException;
import es.progcipfpbatoi.todolistspring.exceptions.NotFoundException;
import es.progcipfpbatoi.todolistspring.model.entities.Tarea;
import jakarta.annotation.PreDestroy;
import org.garret.perst.IterableIterator;
import org.garret.perst.Storage;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Task storage backed by a Perst object database saved in a local file.
 */
@Repository
@Primary
public class TareaRepositoryPerst implements ITareaRepository {
	private final Storage db;
	private final RootContainer root;
	private final PerstConnection conexion;

	/**
	 * Opens the Perst connection and keeps its storage and root.
	 */
	public TareaRepositoryPerst() {
		this.conexion = new PerstConnection();
		this.db = conexion.getStorage();
		this.root = conexion.getRoot();
	}

	/**
	 * Closes the database when Spring shuts the app down.
	 */
	@PreDestroy
	public void destroy() {
		conexion.close();
	}

	/**
	 * Works out the next free code (highest code + 1).
	 *
	 * @return the next available code
	 */
	private int siguienteCodigo() {
		int max = 0;
		for (Tarea t : root.getTareasByCod()) {
			if (t.codigo > max)
				max = t.codigo;
		}
		return max + 1;
	}

	/**
	 * Returns every stored task.
	 *
	 * @return the list of tasks
	 */
	@Override
	public ArrayList<Tarea> findAll() {
		try {
			ArrayList<Tarea> result = new ArrayList<>();
			for (Tarea t : root.getTareasByCod()) {
				result.add(t);
			}
			return result;
		} catch (Exception e) {
			throw new DatabaseErrorException("Error getting the list: " + e.getMessage(), e);
		}
	}

	/**
	 * Gets a task by its code.
	 *
	 * @param codTarea the task's code
	 * @return the task
	 * @throws NotFoundException if there's no task with that code
	 */
	@Override
	public Tarea get(int codTarea) throws NotFoundException {
		try {
			Tarea task = root.getTareasByCod().get(codTarea);
			if (task == null) {
				throw new NotFoundException("The task with code " + codTarea + " doesn't exist");
			}
			return task;
		} catch (NotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseErrorException("Error searching for the task: " + e.getMessage(), e);
		}
	}

	/**
	 * Searches with a JSQL query for user and done status, and filters the date by hand.
	 *
	 * @param usuario the user to filter by
	 * @param fechaVencimiento the due date to filter by
	 * @param realizada whether the task is done
	 * @return the matching tasks
	 */
	@Override
	public ArrayList<Tarea> findByFiltros(String usuario, LocalDate fechaVencimiento, Boolean realizada) {
		try {
			StringBuilder predicate = new StringBuilder("1=1");

			if (usuario != null && !usuario.isBlank()) {
				predicate.append(" and nombre = '").append(usuario).append("'");
			}
			if (realizada != null) {
				predicate.append(" and realizada = ").append(realizada);
			}

			IterableIterator<Tarea> iterator = root.getTareasByCod().select(Tarea.class, predicate.toString());

			ArrayList<Tarea> result = new ArrayList<>();
			for (Tarea t : iterator) {
				// Manual date filter (JSQL doesn't support LocalDate)
				if (fechaVencimiento == null || t.vencimientoEn.toLocalDate().equals(fechaVencimiento)) {
					result.add(t);
				}
			}
			return result;
		} catch (Exception e) {
			throw new DatabaseErrorException("Error in the search: " + e.getMessage(), e);
		}
	}

	/**
	 * Saves a new task with the next free code, inside a transaction.
	 *
	 * @param tarea the task to save
	 * @throws DuplicatedTaskException if the code is already in use
	 */
	@Override
	public void add(Tarea tarea) throws DuplicatedTaskException {
		try {
			db.beginThreadTransaction(Storage.EXCLUSIVE_TRANSACTION);

			tarea.setCodigo(siguienteCodigo());

			boolean inserted = root.getTareasByCod().put(tarea);
			if (!inserted) {
				throw new DuplicatedTaskException();
			}

			db.commit();

		} catch (DuplicatedTaskException e) {
			db.rollbackThreadTransaction();
			throw e;
		} catch (Exception e) {
			db.rollbackThreadTransaction();
			throw new DatabaseErrorException("Error saving the task: " + e.getMessage(), e);
		} finally {
			db.endThreadTransaction();
		}
	}

	/**
	 * Deletes a task inside a transaction and returns it.
	 *
	 * @param codTarea the code of the task to delete
	 * @return the deleted task
	 * @throws NotFoundException if there's no task with that code
	 */
	@Override
	public Tarea delete(int codTarea) throws NotFoundException {
		Tarea task = get(codTarea);
		try {
			db.beginThreadTransaction(Storage.EXCLUSIVE_TRANSACTION);

			root.getTareasByCod().remove(task);
			db.deallocate(task);

			db.commit();
			return task;

		} catch (Exception e) {
			db.rollbackThreadTransaction();
			throw new DatabaseErrorException("Error deleting the task: " + e.getMessage(), e);
		} finally {
			db.endThreadTransaction();
		}
	}
}