package modelo;

/**
 * Esta clase es el "Jefe".
 * Hereda de usuario, ya que el Administrador es otro usuario mas pero son mas permisos.
 */
public class Administrador extends Usuario {

    private String password;

    // Constructor
    public Administrador(int id, String nombre, String password) {
        super(id, nombre);
        this.password = password;
    }

    /**
     * Metodo de "login".
     * Compara la contraseña que nos pasan con la que tiene el admin guardada.
     * Si coinciden, tiene acceso al sistema avanzado.
     */
    public boolean login(String password) {
        return this.password.equals(password);
    }

    /**
     * Metodo para que el administrador para que pueda gestionar productos.
     * Por ahora solo avisa por consola.
     */
    public void gestionarProducto() {
        System.out.println("Gestionando productos...");
    }
    
    /**
     * Parte fundamental: Cierre que caja.
     * Solo el administrador puede hacer el cierre de caja y ver el total de ventas.
     */
    public void verCierreCaja() {
        System.out.println("Mostrando cierre de caja...");
    }
}