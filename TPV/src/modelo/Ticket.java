package modelo;

import java.util.ArrayList;

public class Ticket {
    private int numeroTicket;
    private ArrayList<Producto> productos;
    private double total;
    private String observaciones;
    private Mesa mesa;

    public Ticket(int numeroTicket, Mesa mesa) {
        this.numeroTicket = numeroTicket;
        this.mesa = mesa;
        this.productos = new ArrayList<>();
        this.total = 0;
        this.observaciones = "";
    }

    public void añadirProducto(Producto producto) {
        productos.add(producto);
        calcularTotal();
    }

    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
        calcularTotal();
    }

    public void calcularTotal() {
        total = 0;
        for (Producto p : productos) {
            total += p.getPrecio();
        }
    }

    public void aplicarDescuento(double porcentaje) {
        total = total - (total * porcentaje / 100);
    }

    public double dividirCuenta(int personas) {
        return total / personas;
    }

    public void cobrar() {
        System.out.println("Ticket cobrado.");
        mesa.cambiarEstado(enums.EstadoMesa.PENDIENTE_PAGO);
    }

    public int getNumeroTicket() {
        return numeroTicket;
    }

    public double getTotal() {
        return total;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TICKET Nº ").append(numeroTicket).append("\n");
        sb.append("Mesa: ").append(mesa.getNumero()).append("\n");

        for (Producto p : productos) {
            sb.append(p).append("\n");
        }

        sb.append("TOTAL: ").append(total).append("€");
        return sb.toString();
    }
}