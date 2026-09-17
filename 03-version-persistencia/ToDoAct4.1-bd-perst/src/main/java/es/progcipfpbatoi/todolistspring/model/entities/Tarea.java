package es.progcipfpbatoi.todolistspring.model.entities;

import org.garret.perst.Persistent;
import java.time.LocalDateTime;

public class Tarea extends Persistent {

    public int codigo;
    public String nombre;
    public String descripcion;
    public LocalDateTime creadoEn;
    public LocalDateTime vencimientoEn;
    public Prioritat prioridad;
    public boolean realizada;
    public String categoria;

    public Tarea() {}

    public Tarea(int codigo, String nombre, String descripcion,
                 LocalDateTime vencimientoEn, Prioritat prioridad,
                 boolean realizada) {
        this.codigo       = codigo;
        this.nombre       = nombre;
        this.descripcion  = descripcion;
        this.creadoEn     = LocalDateTime.now();
        this.vencimientoEn = vencimientoEn;
        this.prioridad    = prioridad;
        this.realizada    = realizada;
    }

    public int getCodigo()               { return codigo; }
    public String getNombre()            { return nombre; }
    public String getDescripcion()       { return descripcion; }
    public LocalDateTime getCreadoEn()   { return creadoEn; }
    public LocalDateTime getVencimientoEn() { return vencimientoEn; }
    public Prioritat getPrioridad()      { return prioridad; }
    public boolean isRealizada()         { return realizada; }
    public String getCategoria()         { return categoria; }

    public void setCodigo(int codigo)             { this.codigo = codigo; }
    public void setNombre(String nombre)          { this.nombre = nombre; }
    public void setDescripcion(String d)          { this.descripcion = d; }
    public void setCreadoEn(LocalDateTime c)      { this.creadoEn = c; }
    public void setVencimientoEn(LocalDateTime v) { this.vencimientoEn = v; }
    public void setPrioridad(Prioritat p)         { this.prioridad = p; }
    public void setRealizada(boolean r)           { this.realizada = r; }
    public void setCategoria(String c)            { this.categoria = c; }
}