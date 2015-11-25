package com.example.dam.recetario.receta_ingrediente;

public class RecetaIngrediente {

    private long id, idReceta, idIngrediente;
    private String cantidad;

    public RecetaIngrediente() {
        this(0,0,"");
    }

    public RecetaIngrediente(long idReceta, long idIngrediente, String cantidad) {
        this.id = 0;
        this.idReceta = idReceta;
        this.idIngrediente = idIngrediente;
        this.cantidad = cantidad;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(long idReceta) {
        this.idReceta = idReceta;
    }

    public long getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(long idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecetaIngrediente recetaIngrediente = (RecetaIngrediente) o;

        return id == recetaIngrediente.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "RecetaIngrediente{" +
                "id=" + id +
                ", idReceta=" + idReceta +
                ", idIngrediente=" + idIngrediente +
                ", cantidad=" + cantidad +
                '}';
    }
}
