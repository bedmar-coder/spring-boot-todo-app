package es.progcipfpbatoi.todolistspring.model.repositories;

import es.progcipfpbatoi.todolistspring.exceptions.DuplicatedTaskException;
import es.progcipfpbatoi.todolistspring.exceptions.NotFoundException;
import es.progcipfpbatoi.todolistspring.model.entities.Prioritat;
import es.progcipfpbatoi.todolistspring.model.entities.Tarea;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * In-memory task storage. The data is lost every time the app restarts.
 */
@Repository
public class TareaRepository {

	private ArrayList<Tarea> tareas;
	private final AtomicInteger contador = new AtomicInteger(1);

	/**
	 * Creates the storage with a few sample tasks.
	 */
	public TareaRepository() {
		this.tareas = new ArrayList<>();

		tareas.add(new Tarea(contador.getAndIncrement(), "Juan", "Basketball game",
				LocalDateTime.of(2026, 5, 12, 20, 0), Prioritat.ALTA, false));
		tareas.add(new Tarea(contador.getAndIncrement(), "Juan", "Study Programming",
				LocalDateTime.of(2026, 5, 18, 20, 0), Prioritat.MITJANA, false));
		tareas.add(new Tarea(contador.getAndIncrement(), "Batoi", "Make lunch",
				LocalDateTime.of(2026, 5, 12, 20, 0), Prioritat.BAIXA, true));
		tareas.add(new Tarea(contador.getAndIncrement(), "Elena", "Trim the garden hedges",
				LocalDateTime.of(2026, 5, 10, 7, 52), Prioritat.MITJANA, false));
	}

	/**
	 * Adds the given task to the in-memory database, giving it a new code.
	 *
	 * @param tarea the task to add
	 * @throws DuplicatedTaskException if an equal task is already stored
	 */
	public void add(Tarea tarea) throws DuplicatedTaskException {
		for (Tarea elem : tareas) {
			if (elem.equals(tarea)) {
				throw new DuplicatedTaskException();
			}
		}
		tarea.setCodigo(contador.getAndIncrement());
		this.tareas.add(tarea);
	}

	/**
	 * Gets the task with the given code.
	 *
	 * @param codTarea the code of the task to look for
	 * @return the task with that code
	 * @throws NotFoundException if there's no task with that code
	 */
	public Tarea get(int codTarea) throws NotFoundException {
		for (Tarea t : tareas) {
			if (t.getCodigo() == codTarea) {
				return t;
			}
		}
		throw new NotFoundException("The task with code " + codTarea + " doesn't exist");
	}

	/**
	 * Returns the list of all the tasks.
	 *
	 * @return a copy of the task list
	 */
	public ArrayList<Tarea> findAll() {
		return new ArrayList<>(tareas);
	}

	/**
	 * Returns all the tasks whose user (nombre) matches the given one.
	 *
	 * @param user the user to filter by (empty or null returns every task)
	 * @return the matching tasks
	 */
	public ArrayList<Tarea> findAll(String user) {

		if (user == null || user.isBlank()) {
			return new ArrayList<>(tareas);
		}

		return tareas.stream()
				.filter(t -> t.getNombre().equalsIgnoreCase(user))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Filters the tasks with the given data. Any filter left empty is ignored.
	 *
	 * @param usuario the user to filter by
	 * @param fechaVencimiento the due date to filter by
	 * @param realizada whether the task is done
	 * @return the matching tasks
	 */
	public ArrayList<Tarea> findByFiltros(String usuario, LocalDate fechaVencimiento, Boolean realizada) {
		return tareas.stream()
				.filter(t -> usuario == null || usuario.isBlank() || t.getNombre().equalsIgnoreCase(usuario))
				.filter(t -> fechaVencimiento == null || t.getVencimientoEn().toLocalDate().equals(fechaVencimiento))
				.filter(t -> realizada == null || t.isRealizada() == realizada)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Deletes the task with the given code and returns it.
	 *
	 * @param codTarea the code of the task to delete
	 * @return the deleted task
	 * @throws NotFoundException if there's no task with that code
	 */
	public Tarea delete(int codTarea) throws NotFoundException {
		for (int i = 0; i < tareas.size(); i++) {
			if (tareas.get(i).getCodigo() == codTarea) {
				return tareas.remove(i);
			}
		}
		throw new NotFoundException("The task with code " + codTarea + " doesn't exist");
	}

}
