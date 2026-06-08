package modelo;

import excepciones.MesaException;
import ui.VentanaMesas;

/**
 * Representa una mesa física dentro de la sala del restaurante.
 * Es una clase clave porque actúa como "puente" entre el mundo visual (los botones azules o rojos)
 * y la lógica de negocio (el ticket con la cuenta y los productos).
 */
public class Mesa extends VentanaMesas {

    // ATRIBUTOS
    private int numero;
    private EstadoMesa estado;

    // Una mesa tiene un ticket, aquí es donde se van guardando lo que va pidiendo.
    private Ticket ticketActivo;

    /**
     * Constructor para instanciar una nueva mesa al abrir el programa.
     * @param numero El número físico de la mesa (del 1 al 11 en nuestro caso).
     */
    public Mesa(int numero) {
        this.numero = numero;

        // Por defecto, cuando abrimos el restaurante, todas las mesas están libres
        this.estado = EstadoMesa.LIBRE;

        // Al empezar, le ponemos un ticket en blanco asociado a su número.
        // Así nos aseguramos de que el ticketActivo NUNCA sea null y nos ahorramos muchos NullPointerExceptions.
        this.ticketActivo = new Ticket(numero);
    }

    // getters y setters

    public int getNumero() {
        return numero;
    }

    public EstadoMesa getEstado() {
        return estado;
    }

    /**
     * Permite modificar el estado de la mesa (Ej: pasarla de LIBRE a OCUPADA).
     * @param nuevoEstado El nuevo estado (sacado de nuestro Enum).
     */
    public void cambiarEstado(EstadoMesa nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public Ticket getTicketActivo() { return ticketActivo; }

    // MÉTODOS DE LÓGICA DE NEGOCIO

    /**
     * Resetea la mesa para dejarla lista para los siguientes clientes.
     * @throws MesaException Si intentamos vaciar la mesa saltándonos el proceso de pago.
     */
    public void liberarMesa() throws MesaException {
        // Control de errores de negocio:
        // Si el camarero le ha dado a "Cobrar" pero el proceso no ha terminado (PENDIENTE_PAGO),
        // lanzamos nuestra propia excepción para abortar la operación y que no se pierda la cuenta.
        if (this.estado == EstadoMesa.PENDIENTE_PAGO) {
            throw new MesaException("La mesa tiene pagos pendientes y no se puede liberar.");
        }

        // Si todo está correcto, volvemos a poner la mesa disponible
        this.estado = EstadoMesa.LIBRE;

        // "Vaciamos" la cuenta pisando el ticket antiguo con uno completamente nuevo y en blanco,
        // listo para que el próximo grupo de clientes empiece a pedir.
        this.ticketActivo = new Ticket(numero);
    }
}