import java.util.ArrayList;

public class Ticket {
    private int numeroTicket;
    private ArrayList<Producto> productos = new ArrayList<>();
    private float total;
    private String observaciones;

    public void añadirProducto(Producto p) {
        productos.add(p);
        calcularTotal();
    }

    public void calcularTotal() {
        this.total = 0;
        for (Producto p : productos) {
            this.total += p.getPrecio();
        }
    }

    public void aplicarDescuento(float porcentaje) {
        this.total -= (this.total * (porcentaje / 100));
    }
}
