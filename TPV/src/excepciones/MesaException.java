package excepciones;

/**
 * Excepción personalizadapara el control de errores de la sala.
 * Al heredar de la clase base 'Exception', la convertimos en unaExcepción comprobada,
 * lo que obliga a quien use el método a poner un bloque try-catch por obligación.
 */
public class MesaException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor de la excepción.
     * @param mensaje El texto descriptivo del error (Ej: "La mesa tiene pagos pendientes").
     */
    public MesaException(String mensaje) {
        // Usamos super() para enviarle el texto del error a la clase padre (Exception).
        // Así, cuando en el try-catch hagamos un 'e.getMessage()', nos devolverá este texto exacto.
        super(mensaje);
    }
}