package modelo;

/**
 * Representa al perfil con mayores privilegios dentro del sistema TPV (el gerente o dueño).
 * Al usar la palabra reservada 'extends', esta clase hereda automáticamente de 'Usuario'.
 * Esto significa que adquiere todos sus atributos (id, nombre, password) y métodos (getters/setters)
 * sin tener que volver a escribirlos. ¡Reutilización de código al máximo!
 */
public class Administrador extends Usuario {

    /**
     * Constructor para crear un nuevo perfil de Administrador en el programa.
     * * @param id Identificador único que viene de la base de datos (MariaDB).
     * @param nombre El nombre o alias del administrador (ej: "admin").
     * @param password La contraseña secreta para entrar al panel de control.
     */
    public Administrador(int id, String nombre, String password) {
        // La instrucción super() invoca inmediatamente al constructor de la clase "padre" (Usuario).
        // Le pasamos los 3 parámetros para que el padre se encargue de inicializar las variables,
        // ya que nosotros no las hemos declarado en esta clase, sino en la clase superior.
        super(id, nombre, password);
    }
}