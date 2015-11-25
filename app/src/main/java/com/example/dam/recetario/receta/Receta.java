package com.example.dam.recetario.receta;

import java.io.Serializable;

public class Receta implements Serializable {
    private long id;
    private String nombre, foto, instrucciones;

    public Receta() {
        this("","","");
    }

    public Receta(String nombre, String foto, String instrucciones) {
        this.id = 0;
        this.nombre = nombre;
        this.foto = foto;
        this.instrucciones = instrucciones;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receta receta = (Receta) o;

        return id == receta.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", foto='" + foto + '\'' +
                ", instrucciones='" + instrucciones + '\'' +
                '}';
    }
}
