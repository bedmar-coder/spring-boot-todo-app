package es.progcipfpbatoi.todolistspring.model.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Tarea {

    private int codigo;
    private String nombre;       
    private String descripcion;
    private LocalDateTime creadoEn;
    private LocalDateTime vencimientoEn;
    private Prioritat prioridad;
    private boolean realizada;
    private String categoria;    

   
    public Tarea() {
        this.creadoEn = LocalDateTime.now();
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tarea other = (Tarea) obj;
        return codigo == other.codigo;
    }
}