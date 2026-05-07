public class SistemaTPV {
    private Date fechaSesion;
    private float totalCaja;
    private int numeroTickets;

    public void iniciarSesion() {
        this.fechaSesion = new Date();
        this.totalCaja = 0;
    }

    public void registrarTicket(Ticket t) {
        this.totalCaja += t.getTotal();
        this.numeroTickets++;
    }
}
