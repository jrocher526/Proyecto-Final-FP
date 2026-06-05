package modelo;

/**
 * Enumeración para controlar el ciclo de vida y la ocupación de una mesa física.
 */
public enum EstadoMesa {
    LIBRE, // Color azul en la interfaz
    OCUPADA, // Color rojo en la interfaz
    PENDIENTE_PAGO, // Estado intermedio mientras se genera el cobro
}