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

@Repository
public class TareaRepository implements ITareaRepository{

	private ArrayList<Tarea> tareas;
	private final AtomicInteger contador = new AtomicInteger(1);

	public TareaRepository() {
		this.tareas = new ArrayList<>();

		tareas.add(new Tarea(contador.getAndIncrement(), "Juan", "Partido de baloncesto",
				LocalDateTime.of(2026, 5, 12, 20, 0), Prioritat.ALTA, false));
		tareas.add(new Tarea(contador.getAndIncrement(), "Juan", "Estudiar Programación",
				LocalDateTime.of(2026, 5, 18, 20, 0), Prioritat.MEDIA, false));
		tareas.add(new Tarea(contador.getAndIncrement(), "Batoi", "Hacer la comida",
				LocalDateTime.of(2026, 5, 12, 20, 0), Prioritat.BAJA, true));
		tareas.add(new Tarea(contador.getAndIncrement(), "Elena", "Podar los setos del jardín",
				LocalDateTime.of(2026, 5, 10, 7, 52), Prioritat.MEDIA, false));
	}

	/**
	 * Añade la Tarea recibida como argumento a la base de datos en memoria
	 * 
	 * @param tarea
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
	 * Obtiene la Tarea con codigo @codTarea. En caso de que no la encuentre
	 * devolverá una excepción
	 * 
	 * @NotFoundException
	 *
	 * @param codTarea
	 */
	public Tarea get(int codTarea) throws NotFoundException {
		for (Tarea t : tareas) {
			if (t.getCodigo() == codTarea) {
				return t;
			}
		}
		throw new NotFoundException("La tasca amb codi " + codTarea + " no existeix");
	}

	/**
	 * Devuelve el listado de todas las tareas.
	 */
	public ArrayList<Tarea> findAll() {
		return new ArrayList<>(tareas);
	}

	/**
	 * Devuelve el listado de todas las tareas cuyo atributo nombre coincide
	 * con @user
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
	 * FIltra con los datos
	 * dados
	 */
	public ArrayList<Tarea> findByFiltros(String usuario, LocalDate fechaVencimiento, Boolean realizada) {
		return tareas.stream()
				.filter(t -> usuario == null || usuario.isBlank() || t.getNombre().equalsIgnoreCase(usuario))
				.filter(t -> fechaVencimiento == null || t.getVencimientoEn().toLocalDate().equals(fechaVencimiento))
				.filter(t -> realizada == null || t.isRealizada() == realizada)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
     * Elimina la tarea con el código dado y la devuelve.
     * Lanza NotFoundException si no existe.
     */
    public Tarea delete(int codTarea) throws NotFoundException {
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).getCodigo() == codTarea) {
                return tareas.remove(i);
            }
        }
        throw new NotFoundException("La tasca amb codi " + codTarea + " no existeix");
    }

}
