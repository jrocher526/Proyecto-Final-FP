package modelo;

import enums.Categoria;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Producto {

    @Id
    private int id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private double precio;

    public Producto() {
    }

    public Producto(int id, String nombre, Categoria categoria, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    @Override
    public String toString() {
        return nombre + " - " + precio + "€";
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}