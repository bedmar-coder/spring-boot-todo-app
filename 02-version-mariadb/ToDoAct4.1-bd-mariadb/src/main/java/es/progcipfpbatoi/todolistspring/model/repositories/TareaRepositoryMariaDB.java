package es.progcipfpbatoi.todolistspring.model.repositories;

import es.progcipfpbatoi.todolistspring.exceptions.DatabaseErrorException;
import es.progcipfpbatoi.todolistspring.exceptions.DuplicatedTaskException;
import es.progcipfpbatoi.todolistspring.exceptions.NotFoundException;
import es.progcipfpbatoi.todolistspring.model.entities.Prioritat;
import es.progcipfpbatoi.todolistspring.model.entities.Tarea;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Task storage backed by a MariaDB database, using plain SQL through JdbcTemplate.
 * Table and column names are kept as they are because they come from the database.
 */
@Repository
@Primary
public class TareaRepositoryMariaDB implements ITareaRepository {

	private final JdbcTemplate jdbc;

	/**
	 * @param jdbc the Spring helper used to run the SQL queries
	 */
	public TareaRepositoryMariaDB(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	// Turns each row of the result into a Tarea object
	private final RowMapper<Tarea> rowMapper = (rs, rowNum) -> {
		Tarea t = new Tarea();
		t.setCodigo(rs.getInt("codigo"));
		t.setNombre(rs.getString("usuario"));
		t.setDescripcion(rs.getString("descripcion"));
		t.setVencimientoEn(rs.getTimestamp("fecha_vencimiento").toLocalDateTime());
		t.setPrioridad(Prioritat.valueOf(rs.getString("prioridad")));
		t.setRealizada(rs.getBoolean("realizada"));
		t.setCategoria(rs.getString("nombre_categoria"));
		return t;
	};

	/**
	 * Returns every task, with its category name, ordered by code.
	 *
	 * @return the list of tasks
	 */
	@Override
	public ArrayList<Tarea> findAll() {
		try {
			String sql = """
					SELECT t.*, c.nombre AS nombre_categoria
					FROM tareas t
					LEFT JOIN categorias c ON t.categoria_id = c.id
					ORDER BY t.codigo
					""";
			return new ArrayList<>(jdbc.query(sql, rowMapper));
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
			String sql = """
					SELECT t.*, c.nombre AS nombre_categoria
					FROM tareas t
					LEFT JOIN categorias c ON t.categoria_id = c.id
					WHERE t.codigo = ?
					""";
			List<Tarea> result = jdbc.query(sql, rowMapper, codTarea);
			if (result.isEmpty()) {
				throw new NotFoundException("The task with code " + codTarea + " doesn't exist");
			}
			return result.get(0);
		} catch (NotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseErrorException("Error searching for the task: " + e.getMessage(), e);
		}
	}

	/**
	 * Builds the SQL query step by step, adding only the filters that were filled in.
	 *
	 * @param usuario the user to filter by
	 * @param fechaVencimiento the due date to filter by
	 * @param realizada whether the task is done
	 * @return the matching tasks
	 */
	@Override
	public ArrayList<Tarea> findByFiltros(String usuario, LocalDate fechaVencimiento, Boolean realizada) {
		try {
			StringBuilder sql = new StringBuilder("""
					SELECT t.*, c.nombre AS nombre_categoria
					FROM tareas t
					LEFT JOIN categorias c ON t.categoria_id = c.id
					WHERE 1=1
					""");
			List<Object> params = new ArrayList<>();

			if (usuario != null && !usuario.isBlank()) {
				sql.append(" AND t.usuario = ?");
				params.add(usuario);
			}
			if (fechaVencimiento != null) {
				sql.append(" AND DATE(t.fecha_vencimiento) = ?");
				params.add(fechaVencimiento.toString());
			}
			if (realizada != null) {
				sql.append(" AND t.realizada = ?");
				params.add(realizada);
			}
			sql.append(" ORDER BY t.codigo");

			return new ArrayList<>(jdbc.query(sql.toString(), rowMapper, params.toArray()));
		} catch (Exception e) {
			throw new DatabaseErrorException("Error in the search: " + e.getMessage(), e);
		}
	}

	/**
	 * Inserts a new task, looking up the id of its category first.
	 *
	 * @param tarea the task to insert
	 * @throws DuplicatedTaskException declared by the interface
	 */
	@Override
	public void add(Tarea tarea) throws DuplicatedTaskException {
		try {

			Integer categoryId = null;
			if (tarea.getCategoria() != null && !tarea.getCategoria().isBlank()) {
				List<Integer> ids = jdbc.query("SELECT id FROM categorias WHERE nombre = ?",
						(rs, rn) -> rs.getInt("id"), tarea.getCategoria());
				if (!ids.isEmpty()) {
					categoryId = ids.get(0);
				}
			}

			String sql = """
					INSERT INTO tareas (usuario, descripcion, fecha_vencimiento,
					                    prioridad, realizada, categoria_id)
					VALUES (?, ?, ?, ?, ?, ?)
					""";
			jdbc.update(sql, tarea.getNombre(), tarea.getDescripcion(), tarea.getVencimientoEn(),
					tarea.getPrioridad().name(), tarea.isRealizada() ? 1 : 0, categoryId);
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage(), e);
		}
	}

	/**
	 * Deletes a task and returns it.
	 *
	 * @param codTarea the code of the task to delete
	 * @return the deleted task
	 * @throws NotFoundException if there's no task with that code
	 */
	@Override
	public Tarea delete(int codTarea) throws NotFoundException {
		Tarea task = get(codTarea);
		try {
			jdbc.update("DELETE FROM tareas WHERE codigo = ?", codTarea);
			return task;
		} catch (Exception e) {
			throw new DatabaseErrorException("Error deleting the task: " + e.getMessage(), e);
		}
	}
}