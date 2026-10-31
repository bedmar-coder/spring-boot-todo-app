package es.progcipfpbatoi.todolistspring.model.repositories;

import es.progcipfpbatoi.todolistspring.model.entities.Tarea;
import org.garret.perst.FieldIndex;
import org.garret.perst.Persistent;
import org.garret.perst.Storage;

/**
 * Root object of the Perst database: holds the index of tasks by code.
 */
public class RootContainer extends Persistent {

    private FieldIndex<Tarea> tareasByCod;

    /**
     * Creates the root with a unique index on the task's "codigo" field.
     *
     * @param db the open Perst storage
     */
    public RootContainer(Storage db) {
        tareasByCod = db.createFieldIndex(Tarea.class, "codigo", true);
    }

    /**
     * Empty constructor needed by Perst to load the root from the database.
     */
    public RootContainer() {}

    public FieldIndex<Tarea> getTareasByCod() {
        return tareasByCod;
    }
}