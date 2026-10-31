package es.progcipfpbatoi.todolistspring.model.repositories;

import es.progcipfpbatoi.todolistspring.exceptions.DuplicatedTaskException;
import es.progcipfpbatoi.todolistspring.exceptions.NotFoundException;
import es.progcipfpbatoi.todolistspring.model.entities.Tarea;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Operations every task storage must provide, so the controller doesn't
 * depend on where the tasks are actually saved.
 */
public interface ITareaRepository {

	/**
	 * Saves a new task.
	 *
	 * @param tarea the task to save
	 * @throws DuplicatedTaskException if the task already exists
	 */
	void add(Tarea tarea) throws DuplicatedTaskException;

	/**
	 * Gets a task by its code.
	 *
	 * @param codTarea the task's code
	 * @return the task
	 * @throws NotFoundException if there's no task with that code
	 */
	Tarea get(int codTarea) throws NotFoundException;

	/**
	 * Returns every task.
	 *
	 * @return the list of tasks
	 */
	ArrayList<Tarea> findAll();

	/**
	 * Returns the tasks that match the given filters (empty filters are ignored).
	 *
	 * @param usuario the user to filter by
	 * @param fechaVencimiento the due date to filter by
	 * @param realizada whether the task is done
	 * @return the matching tasks
	 */
	ArrayList<Tarea> findByFiltros(String usuario, LocalDate fechaVencimiento, Boolean realizada);

	/**
	 * Deletes a task and returns it.
	 *
	 * @param codTarea the code of the task to delete
	 * @return the deleted task
	 * @throws NotFoundException if there's no task with that code
	 */
	Tarea delete(int codTarea) throws NotFoundException;
}