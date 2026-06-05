package modelo;

import java.util.Objects;

/**
 * Clase abstracta que sirve como "plantilla" base para cualquier empleado del sistema.
 * Agrupa los datos comunes que comparten todos los trabajadores.
 */
public abstract class Usuario {

    // Atributos privados para encapsulación.
    // Solo se podrán modificar o leer a través de sus getters y setters.
    private int id;
    private String nombre;
    private String password;

    /**
     * Constructor principal para instanciar un usuario con todos sus datos.
     * @param id Identificador único en la base de datos.
     * @param nombre Nombre completo o alias del empleado.
     * @param password Contraseña para acceder al TPV.
     */
    public Usuario(int id, String nombre, String password) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
    }

    // GETTERS Y SETTERS

    /**
     * @return El ID del usuario.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id El nuevo ID a asignar.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return El nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre El nuevo nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return La contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password La nueva contraseña.
     */
    public void setPassword(String password) {
        this.password = password;
    }


    //MÉTODOS SOBRESCRITOS

    /**
     * Sobrescribimos el método equals para poder comparar dos usuarios.
     * Para nuestro programa, dos usuarios son el mismo si tienen el mismo ID y el mismo nombre.
     */
    @Override
    public boolean equals(Object o) {
        // Si apuntan exactamente a la misma posición de memoria, son el mismo objeto
        if (this == o) return true;

        // Si el objeto a comparar es nulo o no son de la misma clase, devolvemos false
        if (o == null || getClass() != o.getClass()) return false;

        // Hacemos cast para tratar al objeto genérico como un Usuario
        Usuario usuario = (Usuario) o;

        // Comparamos sus atributos clave (ID y nombre)
        return id == usuario.id && Objects.equals(nombre, usuario.nombre);
    }

    /**
     * Sobrescribimos el hashCode para que vaya en pareja con el equals.
     * Esto es súper importante si metemos Usuarios dentro de un HashMap o un HashSet,
     * para que Java sepa encontrarlos rápido y no duplique datos.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }
}