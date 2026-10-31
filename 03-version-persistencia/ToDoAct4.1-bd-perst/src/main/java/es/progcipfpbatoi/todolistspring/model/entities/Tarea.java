package es.progcipfpbatoi.todolistspring.model.entities;

import org.garret.perst.Persistent;
import java.time.LocalDateTime;

/**
 * A task (tarea) that Perst can store. The field names are kept in Spanish
 * because Perst saves them in the database file and the HTML templates use them.
 */
public class Tarea extends Persistent {

    public int codigo;
    public String nombre;
    public String descripcion;
    public LocalDateTime creadoEn;
    public LocalDateTime vencimientoEn;
    public Prioritat prioridad;
    public boolean realizada;
    public String categoria;

    /**
     * Empty constructor needed by Perst to load tasks from the database.
     */
    public Tarea() {}

    /**
     * Creates a task with all its main data.
     *
     * @param codigo the task's code
     * @param nombre the user the task belongs to
     * @param descripcion what the task is about
     * @param vencimientoEn when it's due
     * @param prioridad its priority
     * @param realizada whether it's already done
     */
    public Tarea(int codigo, String nombre, String descripcion,
                 LocalDateTime vencimientoEn, Prioritat prioridad,
                 boolean realizada) {
        this.codigo        = codigo;
        this.nombre        = nombre;
        this.descripcion   = descripcion;
        this.creadoEn      = LocalDateTime.now();
        this.vencimientoEn = vencimientoEn;
        this.prioridad     = prioridad;
        this.realizada     = realizada;
    }

    public int getCodigo()                  { return codigo; }
    public String getNombre()               { return nombre; }
    public String getDescripcion()          { return descripcion; }
    public LocalDateTime getCreadoEn()      { return creadoEn; }
    public LocalDateTime getVencimientoEn() { return vencimientoEn; }
    public Prioritat getPrioridad()         { return prioridad; }
    public boolean isRealizada()            { return realizada; }
    public String getCategoria()            { return categoria; }

    public void setCodigo(int codigo)             { this.codigo = codigo; }
    public void setNombre(String nombre)          { this.nombre = nombre; }
    public void setDescripcion(String d)          { this.descripcion = d; }
    public void setCreadoEn(LocalDateTime c)      { this.creadoEn = c; }
    public void setVencimientoEn(LocalDateTime v) { this.vencimientoEn = v; }
    public void setPrioridad(Prioritat p)         { this.prioridad = p; }
    public void setRealizada(boolean r)           { this.realizada = r; }
    public void setCategoria(String c)            { this.categoria = c; }
}