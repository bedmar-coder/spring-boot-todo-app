package es.progcipfpbatoi.todolistspring.model.repositories;

import es.progcipfpbatoi.todolistspring.model.entities.Prioritat;
import es.progcipfpbatoi.todolistspring.model.entities.Tarea;
import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;

import java.io.Closeable;
import java.time.LocalDateTime;

public class PerstConnection implements Closeable {

    private static Storage db = null;
    private static RootContainer root;
    private static final String DB_FILE = "tasques.dbs";

    public PerstConnection() {
        try {
            db = StorageFactory.getInstance().createStorage();
            // Abrimos BD con 8MB de caché, si no existe la crea
            db.open(DB_FILE, 8 * 1024 * 1024);

            // Iniciamos transacción
            db.beginThreadTransaction(Storage.EXCLUSIVE_TRANSACTION);

            root = (RootContainer) db.getRoot();

            // Si no existe el root, lo creamos y asignamos
            if (root == null) {
                root = new RootContainer(db);
                db.setRoot(root);
            }

            // Si la BD está vacía, cargamos datos de ejemplo
            if (root.getTareasByCod().isEmpty()) {
                initTareas();
            }

            db.commit();

        } catch (Exception e) {
            db.rollbackThreadTransaction();
        } finally {
            db.endThreadTransaction();
        }
    }

    private void initTareas() {
        Tarea t1 = new Tarea(1, "Juan", "Partido de baloncesto",
            LocalDateTime.of(2026, 5, 12, 20, 0), Prioritat.ALTA, false);
        t1.setCategoria("Deporte");

        Tarea t2 = new Tarea(2, "Juan", "Estudiar Programación",
            LocalDateTime.of(2026, 5, 18, 20, 0), Prioritat.MEDIA, false);
        t2.setCategoria("Escola");

        Tarea t3 = new Tarea(3, "Batoi", "Hacer la comida",
            LocalDateTime.of(2026, 5, 12, 20, 0), Prioritat.BAJA, true);
        t3.setCategoria("Tareas del Hogar");

        Tarea t4 = new Tarea(4, "Elena", "Podar los setos del jardín",
            LocalDateTime.of(2026, 5, 10, 7, 52), Prioritat.MEDIA, false);
        t4.setCategoria("Jardinería");

        // put() ya llama a store() internamente
        root.getTareasByCod().put(t1);
        root.getTareasByCod().put(t2);
        root.getTareasByCod().put(t3);
        root.getTareasByCod().put(t4);
    }

    public Storage getStorage() {
        return db;
    }

    public RootContainer getRoot() {
        return root;
    }

    @Override
    public void close() {
        if (db != null && db.isOpened()) {
            db.close();
        }
    }
}