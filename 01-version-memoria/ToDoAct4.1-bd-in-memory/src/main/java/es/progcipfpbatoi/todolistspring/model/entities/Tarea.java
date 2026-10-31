package es.progcipfpbatoi.todolistspring.model.entities;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A task (tarea): who it belongs to, what it is, when it's due, its priority,
 * whether it's done and its category. The field names are kept in Spanish
 * because the HTML templates use them.
 */
public class Tarea {

    private int codigo;
    private String nombre;
    private String descripcion;
    private LocalDateTime creadoEn;
    private LocalDateTime vencimientoEn;
    private Prioritat prioridad;
    private boolean realizada;
    private String categoria;


    /**
     * Creates an empty task, with its creation date set to now.
     */
    public Tarea() {
        this.creadoEn = LocalDateTime.now();
    }

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
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creadoEn = LocalDateTime.now();
        this.vencimientoEn = vencimientoEn;
        this.prioridad = prioridad;
        this.realizada = realizada;
    }


    public int getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
    public LocalDateTime getVencimientoEn() { return vencimientoEn; }
    public Prioritat getPrioridad() { return prioridad; }
    public boolean isRealizada() { return realizada; }
    public String getCategoria() { return categoria; }


    public void setCodigo(int codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
    public void setVencimientoEn(LocalDateTime vencimientoEn) { this.vencimientoEn = vencimientoEn; }
    public void setPrioridad(Prioritat prioridad) { this.prioridad = prioridad; }
    public void setRealizada(boolean realizada) { this.realizada = realizada; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public int hashCode() { return Objects.hash(codigo); }

    /**
     * Two tasks are equal when they have the same code.
     *
     * @param obj the object to compare with
     * @return true if it's a task with the same code
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tarea other = (Tarea) obj;
        return codigo == other.codigo;
    }
}