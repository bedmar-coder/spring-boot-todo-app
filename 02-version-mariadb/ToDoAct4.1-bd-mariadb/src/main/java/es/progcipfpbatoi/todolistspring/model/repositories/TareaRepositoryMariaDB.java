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

@Repository
@Primary
public class TareaRepositoryMariaDB implements ITareaRepository {

	private final JdbcTemplate jdbc;

	public TareaRepositoryMariaDB(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

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
			throw new DatabaseErrorException("Error al obtenir el llistat: " + e.getMessage(), e);
		}
	}

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
				throw new NotFoundException("La tasca amb codi " + codTarea + " no existeix");
			}
			return result.get(0);
		} catch (NotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseErrorException("Error al cercar la tasca: " + e.getMessage(), e);
		}
	}

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
			throw new DatabaseErrorException("Error en la cerca: " + e.getMessage(), e);
		}
	}

	@Override
	public void add(Tarea tarea) throws DuplicatedTaskException {
		try {

			Integer categoriaId = null;
			if (tarea.getCategoria() != null && !tarea.getCategoria().isBlank()) {
				List<Integer> ids = jdbc.query("SELECT id FROM categorias WHERE nombre = ?",
						(rs, rn) -> rs.getInt("id"), tarea.getCategoria());
				if (!ids.isEmpty()) {
					categoriaId = ids.get(0);
				}
			}

			String sql = """
					INSERT INTO tareas (usuario, descripcion, fecha_vencimiento,
					                    prioridad, realizada, categoria_id)
					VALUES (?, ?, ?, ?, ?, ?)
					""";
			jdbc.update(sql, tarea.getNombre(), tarea.getDescripcion(), tarea.getVencimientoEn(),
					tarea.getPrioridad().name(), tarea.isRealizada() ? 1 : 0, categoriaId);
		} catch (Exception e) {
			throw new DatabaseErrorException(e.getMessage(), e);
		}
	}

	@Override
	public Tarea delete(int codTarea) throws NotFoundException {
		Tarea tarea = get(codTarea);
		try {
			jdbc.update("DELETE FROM tareas WHERE codigo = ?", codTarea);
			return tarea;
		} catch (Exception e) {
			throw new DatabaseErrorException("Error al esborrar la tasca: " + e.getMessage(), e);
		}
	}
}