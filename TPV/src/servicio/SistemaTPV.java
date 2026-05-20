package servicio;

import modelo.Mesa;
import modelo.Ticket;

import java.util.ArrayList;
import java.util.Date;

public class SistemaTPV {
    private Date fechaSesion;
    private float totalCaja;
    private int numeroTickets;

    private ArrayList<Ticket> tickets;
    private ArrayList<Mesa> mesas;

    // constructor
    public SistemaTPV() {
        tickets = new ArrayList<>();
        mesas = new ArrayList<>();
        totalCaja = 0;
        numeroTickets = 0;
    }

    // metodos
    public void iniciarSesion() {
        this.fechaSesion = new Date();
        this.totalCaja = 0;
    }

    public void registrarTicket(Ticket t) {
        tickets.add(t);
        this.totalCaja += t.getTotal();
        this.numeroTickets++;
    }

    public void añadirMesa(Mesa mesa) {
        mesas.add(mesa);
    }

    public void mostrarResumenCaja() {
        System.out.println("RESUMEN DE CAJA");
        System.out.println("Fecha sesión: " + fechaSesion);
        System.out.println("Número de tickets: " + numeroTickets);
        System.out.println("Total caja: " + totalCaja + " €");
    }

    public void cerrarCaja() {
        System.out.println("Caja cerrada correctamente.");
    }

    // getters
    public Date getFechaSesion() {
        return fechaSesion;
    }

    public float getTotalCaja() {
        return totalCaja;
    }

    public int getNumeroTickets() {
        return numeroTickets;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public ArrayList<Mesa> getMesas() {
        return mesas;
    }
}