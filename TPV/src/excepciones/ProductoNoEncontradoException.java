package excepciones;

/**
 * Excepción personalizada para gestionar errores relacionados con el catálogo de productos.
 * Al heredar de 'Exception' , obligamos al código que la invoque
 * a hacerse cargo del error mediante un bloque try-catch, evitando que el programa se cierre de golpe.
 */
public class ProductoNoEncontradoException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor de la excepción.
     * @param mensaje Texto detallado sobre el error (Ej: "El producto 'Fanta' no existe en la base de datos").
     */
    public ProductoNoEncontradoException(String mensaje) {
        // Invocamos al constructor de la clase padre (Exception) pasándole el mensaje.
        // Gracias a esto, quien capture el error podrá leer el texto usando el método e.getMessage().
        super(mensaje);
    }
}