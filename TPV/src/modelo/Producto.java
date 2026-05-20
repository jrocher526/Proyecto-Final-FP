package modelo;

import enums.Categoria;

/**
 * Esta clase representa cualquier articulo que tengamos en la carta.
 * Sirve para mantener los productos ordenados.
 */
public class Producto {

    // Atributos
    private int id;              // Codigo identificador
    private String nombre;
    private Categoria categoria; // Si es bebida, comida, postre, etc.
    private double precio;

    // Constructor
    public Producto(int id, String nombre, Categoria categoria, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    /**
     * Método Para mostrar la información de los productos
     */
    @Override
    public String toString() {
        return nombre + " - " + precio + "€";
    }

    // Getters y Setters
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