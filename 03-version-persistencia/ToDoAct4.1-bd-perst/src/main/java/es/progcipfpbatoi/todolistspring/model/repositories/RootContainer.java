package es.progcipfpbatoi.todolistspring.model.repositories;

import es.progcipfpbatoi.todolistspring.model.entities.Tarea;
import org.garret.perst.FieldIndex;
import org.garret.perst.Persistent;
import org.garret.perst.Storage;

public class RootContainer extends Persistent {

    private FieldIndex<Tarea> tareasByCod;

    public RootContainer(Storage db) {
        tareasByCod = db.createFieldIndex(Tarea.class, "codigo", true);
    }

    public RootContainer() {}

    public FieldIndex<Tarea> getTareasByCod() {
        return tareasByCod;
    }
}