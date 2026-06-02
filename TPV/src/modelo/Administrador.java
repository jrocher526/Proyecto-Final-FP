package modelo;


import javax.persistence.Entity;

@Entity
public class Administrador extends Usuario {

    private String password;

    public Administrador() {
    }

    public Administrador(int id, String nombre, String password) {
        super(id, nombre);
        this.password = password;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }

    public void gestionarProducto() {
        System.out.println("Gestionando productos...");
    }

    public void verCierreCaja() {
        System.out.println("Mostrando cierre de caja...");
    }
}