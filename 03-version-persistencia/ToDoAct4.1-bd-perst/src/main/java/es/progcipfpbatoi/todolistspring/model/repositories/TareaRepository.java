package es.progcipfpbatoi.todolistspring.model.repositories;

import es.progcipfpbatoi.todolistspring.exceptions.DuplicatedTaskException;
import es.progcipfpbatoi.todolistspring.exceptions.NotFoundException;
import es.progcipfpbatoi.todolistspring.model.entities.Tarea;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Empty placeholder repository. The Perst repository is the one used by default.
 */
@Repository
public class TareaRepository implements ITareaRepository {

    /**
     * Does nothing.
     *
     * @param tarea the task (ignored)
     */
    @Override
    public void add(Tarea tarea) throws DuplicatedTaskException {}

    /**
     * Always returns null.
     *
     * @param codTarea the task's code (ignored)
     * @return null
     */
    @Override
    public Tarea get(int codTarea) throws NotFoundException {
        return null;
    }

    /**
     * Always returns an empty list.
     *
     * @return an empty list
     */
    @Override
    public ArrayList<Tarea> findAll() {
        return new ArrayList<>();
    }

    /**
     * Always returns an empty list.
     *
     * @param usuario the user (ignored)
     * @param fechaVencimiento the due date (ignored)
     * @param realizada whether it's done (ignored)
     * @return an empty list
     */
    @Override
    public ArrayList<Tarea> findByFiltros(String usuario, LocalDate fechaVencimiento,
                                           Boolean realizada) {
        return new ArrayList<>();
    }

    /**
     * Always returns null.
     *
     * @param codTarea the task's code (ignored)
     * @return null
     */
    @Override
    public Tarea delete(int codTarea) throws NotFoundException {
        return null;
    }
}