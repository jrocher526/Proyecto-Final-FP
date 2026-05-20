package modelo;

/**
 * Esta clase representa a los trabajadores
 * Hereda de Usuario porque también necesitan su ID y nombre para entrar al sistema.
 */
public class Camarero extends Usuario {

    // Constructor
    public Camarero(int id, String nombre, String password) {
        super(id, nombre);
    }

    /**
     * Este método sirve para que el camarero elija en qué mesa va a servir.
     */
    public void seleccionarMesa(Mesa mesa) {
        System.out.println("Mesa seleccionada: " + mesa.getNumero());
    }

    /**
     * Método para que al crear un ticket, automaticamente
     * cambiamos el estado de la mesa a OCUPADA para que nadie más se siente ahi
     * por error, y generamos el ticket de la cuenta.
     */
    public Ticket crearTicket(int numeroTicket, Mesa mesa) {
        mesa.cambiarEstado(enums.EstadoMesa.OCUPADA);
        return new Ticket(numeroTicket, mesa);
    }
}