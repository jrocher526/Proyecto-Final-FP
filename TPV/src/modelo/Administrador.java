package modelo;

import modelo.Usuario;

public class Administrador extends Usuario {

    public Administrador(int id, String nombre, String password) {
        super(id, nombre, password);
    }

    public void gestionarProducto() {
        System.out.println("Gestionando productos...");
    }

    public void verCierreCaja() {
        System.out.println("Mostrando cierre de caja...");
    }
}