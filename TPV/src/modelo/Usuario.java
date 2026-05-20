package modelo;

/**
 * Esta es la clase padre.
 * De aqui sale lo que tienen en comun los que usan el sistema.
 */
public class Usuario {

    // Usamos protected para que las clases hijas
    // puedan heredar y usar las variables directamente.
    protected int id;
    protected String nombre;

    // El constructor
    public Usuario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Método Para mostrar la informacion de las personas.
     */
    @Override
    public String toString() {
        return "ID: " + id + ", Usuario: " + nombre;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}