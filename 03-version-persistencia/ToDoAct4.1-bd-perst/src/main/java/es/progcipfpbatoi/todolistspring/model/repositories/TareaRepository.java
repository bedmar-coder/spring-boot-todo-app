package es.progcipfpbatoi.todolistspring.model.repositories;

import es.progcipfpbatoi.todolistspring.exceptions.DuplicatedTaskException;
import es.progcipfpbatoi.todolistspring.exceptions.NotFoundException;
import es.progcipfpbatoi.todolistspring.model.entities.Tarea;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public class TareaRepository implements ITareaRepository {

    @Override
    public void add(Tarea tarea) throws DuplicatedTaskException {}

    @Override
    public Tarea get(int codTarea) throws NotFoundException {
        return null;
    }

    @Override
    public ArrayList<Tarea> findAll() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Tarea> findByFiltros(String usuario, LocalDate fechaVencimiento,
                                           Boolean realizada) {
        return new ArrayList<>();
    }

    @Override
    public Tarea delete(int codTarea) throws NotFoundException {
        return null;
    }
}