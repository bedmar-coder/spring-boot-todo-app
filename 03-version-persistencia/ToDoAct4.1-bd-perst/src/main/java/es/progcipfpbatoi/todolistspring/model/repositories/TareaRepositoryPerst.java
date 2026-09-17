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

@Repository
@Primary
public class TareaRepositoryPerst implements ITareaRepository {
	private final Storage db;
	private final RootContainer root;
	private final PerstConnection conexion;

	public TareaRepositoryPerst() {
		this.conexion = new PerstConnection();
		this.db = conexion.getStorage();
		this.root = conexion.getRoot();
	}

	@PreDestroy
	public void destroy() {
		conexion.close();
	}

	// Siguiente código disponible
	private int siguienteCodigo() {
		int max = 0;
		for (Tarea t : root.getTareasByCod()) {
			if (t.codigo > max)
				max = t.codigo;
		}
		return max + 1;
	}

	@Override
	public ArrayList<Tarea> findAll() {
		try {
			ArrayList<Tarea> result = new ArrayList<>();
			for (Tarea t : root.getTareasByCod()) {
				result.add(t);
			}
			return result;
		} catch (Exception e) {
			throw new DatabaseErrorException("Error al obtenir el llistat: " + e.getMessage(), e);
		}
	}

	@Override
	public Tarea get(int codTarea) throws NotFoundException {
		try {
			Tarea tarea = root.getTareasByCod().get(codTarea);
			if (tarea == null) {
				throw new NotFoundException("La tasca amb codi " + codTarea + " no existeix");
			}
			return tarea;
		} catch (NotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseErrorException("Error al cercar la tasca: " + e.getMessage(), e);
		}
	}

	@Override
	public ArrayList<Tarea> findByFiltros(String usuario, LocalDate fechaVencimiento, Boolean realizada) {
		try {
			StringBuilder predicado = new StringBuilder("1=1");

			if (usuario != null && !usuario.isBlank()) {
				predicado.append(" and nombre = '").append(usuario).append("'");
			}
			if (realizada != null) {
				predicado.append(" and realizada = ").append(realizada);
			}

			IterableIterator<Tarea> iterador = root.getTareasByCod().select(Tarea.class, predicado.toString());

			ArrayList<Tarea> result = new ArrayList<>();
			for (Tarea t : iterador) {
				// Filtro de fecha manual (JSQL no soporta LocalDate)
				if (fechaVencimiento == null || t.vencimientoEn.toLocalDate().equals(fechaVencimiento)) {
					result.add(t);
				}
			}
			return result;
		} catch (Exception e) {
			throw new DatabaseErrorException("Error en la cerca: " + e.getMessage(), e);
		}
	}

	@Override
	public void add(Tarea tarea) throws DuplicatedTaskException {
		try {
			db.beginThreadTransaction(Storage.EXCLUSIVE_TRANSACTION);

			tarea.setCodigo(siguienteCodigo());

			boolean insertado = root.getTareasByCod().put(tarea);
			if (!insertado) {
				throw new DuplicatedTaskException();
			}

			db.commit();

		} catch (DuplicatedTaskException e) {
			db.rollbackThreadTransaction();
			throw e;
		} catch (Exception e) {
			db.rollbackThreadTransaction();
			throw new DatabaseErrorException("Error al guardar la tasca: " + e.getMessage(), e);
		} finally {
			db.endThreadTransaction();
		}
	}

	@Override
	public Tarea delete(int codTarea) throws NotFoundException {
		Tarea tarea = get(codTarea);
		try {
			db.beginThreadTransaction(Storage.EXCLUSIVE_TRANSACTION);

			root.getTareasByCod().remove(tarea);
			db.deallocate(tarea);

			db.commit();
			return tarea;

		} catch (Exception e) {
			db.rollbackThreadTransaction();
			throw new DatabaseErrorException("Error al esborrar la tasca: " + e.getMessage(), e);
		} finally {
			db.endThreadTransaction();
		}
	}
}