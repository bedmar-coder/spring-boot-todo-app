package es.progcipfpbatoi.todolistspring.model.repositories;

import es.progcipfpbatoi.todolistspring.exceptions.DuplicatedTaskException;
import es.progcipfpbatoi.todolistspring.exceptions.NotFoundException;
import es.progcipfpbatoi.todolistspring.model.entities.Tarea;

import java.time.LocalDate;
import java.util.ArrayList;

public interface ITareaRepository {

	void add(Tarea tarea) throws DuplicatedTaskException;

	Tarea get(int codTarea) throws NotFoundException;

	ArrayList<Tarea> findAll();

	ArrayList<Tarea> findByFiltros(String usuario, LocalDate fechaVencimiento, Boolean realizada);

	Tarea delete(int codTarea) throws NotFoundException;
}