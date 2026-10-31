package es.progcipfpbatoi.todolistspring.model.repositories;

import es.progcipfpbatoi.todolistspring.model.entities.Prioritat;
import es.progcipfpbatoi.todolistspring.model.entities.Tarea;
import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;

import java.io.Closeable;
import java.time.LocalDateTime;

/**
 * Opens the Perst database file and makes sure it has a root object and some sample data.
 */
public class PerstConnection implements Closeable {

    private static Storage db = null;
    private static RootContainer root;
    private static final String DB_FILE = "tasques.dbs";

    /**
     * Opens (or creates) the database and fills it with sample tasks if it's empty.
     */
    public PerstConnection() {
        try {
            db = StorageFactory.getInstance().createStorage();
            // Open the DB with an 8MB cache; it's created if it doesn't exist
            db.open(DB_FILE, 8 * 1024 * 1024);

            // Start a transaction
            db.beginThreadTransaction(Storage.EXCLUSIVE_TRANSACTION);

            root = (RootContainer) db.getRoot();

            // If there's no root yet, create it and set it
            if (root == null) {
                root = new RootContainer(db);
                db.setRoot(root);
            }

            // If the DB is empty, load the sample data
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

    /**
     * Stores four sample tasks.
     */
    private void initTareas() {
        Tarea t1 = new Tarea(1, "Juan", "Basketball game",
            LocalDateTime.of(2026, 5, 12, 20, 0), Prioritat.ALTA, false);
        t1.setCategoria("Deporte");

        Tarea t2 = new Tarea(2, "Juan", "Study Programming",
            LocalDateTime.of(2026, 5, 18, 20, 0), Prioritat.MEDIA, false);
        t2.setCategoria("Escola");

        Tarea t3 = new Tarea(3, "Batoi", "Make lunch",
            LocalDateTime.of(2026, 5, 12, 20, 0), Prioritat.BAJA, true);
        t3.setCategoria("Tareas del Hogar");

        Tarea t4 = new Tarea(4, "Elena", "Trim the garden hedges",
            LocalDateTime.of(2026, 5, 10, 7, 52), Prioritat.MEDIA, false);
        t4.setCategoria("Jardinería");

        // put() already calls store() internally
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

    /**
     * Closes the database if it's open.
     */
    @Override
    public void close() {
        if (db != null && db.isOpened()) {
            db.close();
        }
    }
}