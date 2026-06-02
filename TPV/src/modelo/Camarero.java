package modelo;

import javax.persistence.Entity;

@Entity
public class Camarero extends Usuario {

    public Camarero() {
    }

    public Camarero(int id, String nombre, String password) {
        super(id, nombre);
    }

    public void seleccionarMesa(Mesa mesa) {
        System.out.println("Mesa seleccionada: " + mesa.getNumero());
    }

    public Ticket crearTicket(int numeroTicket, Mesa mesa) {
        mesa.cambiarEstado(enums.EstadoMesa.OCUPADA);
        return new Ticket(numeroTicket, mesa);
    }
}