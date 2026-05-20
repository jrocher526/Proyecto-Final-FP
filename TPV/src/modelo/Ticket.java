package modelo;

import java.util.ArrayList;

/**
 * Esta clase es el "Ticket" de la cuenta. 
 * Lleva el registro de que se ha pedido, en que mesa y cuanto hay que pagar.
 */
public class Ticket {
    
    // Atributos
    private int numeroTicket;               // ID de la venta
    private ArrayList<Producto> productos;  // lista de comida y bebida
    private double total;                   // Lo que toca pagar 
    private String observaciones;           // Notas tipo: "Sin cebolla"
    private Mesa mesa;                      // La mesa a la que pertenece el ticket

    // Constructor
    public Ticket(int numeroTicket, Mesa mesa) {
        this.numeroTicket = numeroTicket;
        this.mesa = mesa;
        this.productos = new ArrayList<>();
        this.total = 0;
        this.observaciones = "";
    }

    /**
     * Metodo para al añadir un producto a la cuenta
     * se actualice el total.
     */
    public void añadirProducto(Producto producto) {
        productos.add(producto);
        calcularTotal();
    }

    /**
     * Metodo para al quitar un producto de la cuenta
     * se actualice el total.
     */
    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
        calcularTotal();
    }

    /**
     * Metodo para recorrer la cuenta
     * e ir sumando para obtener el total.
     */
    public void calcularTotal() {
        total = 0;
        for (Producto p : productos) {
            total += p.getPrecio();
        }
    }

    /**
     * Metodo para aplicar un descuento al cliente.
     * El porcentaje va de 0 a 100.
     */
    public void aplicarDescuento(double porcentaje) {
        total = total - (total * porcentaje / 100);
    }

    /**
     * Método para dividir la cuenta.
     * Nos dice cuanto le toca a cada uno.
     */
    public double dividirCuenta(int personas) {
        return total / personas;
    }

    /**
     * Metodo para cuando nos pidan la cuenta el estado de la mesa cambie.
     * Se espera a que limpien o que se vayan.
     */
    public void cobrar() {
        System.out.println("Ticket cobrado.");
        mesa.cambiarEstado(enums.EstadoMesa.PENDIENTE_PAGO);
    }

    /**
     * Método que dibuja el ticket con un formato para
     * verlo por consola o imprimirlo.
     */
    @Override
    public String toString() {
        // Usamos StringBuilder ya que es mucho mas eficiente
        // que concatenar Strings dentro de bucles.
        StringBuilder sb = new StringBuilder();
        
        // Cabecera del ticket
        sb.append("TICKET Nº ").append(numeroTicket).append("\n");
        sb.append("Mesa: ").append(mesa.getNumero()).append("\n");
        sb.append("--------------------------\n");

        // Recorremos la lista de productos y añadimos cada uno en una linea nueva
        for (Producto p : productos) {
            sb.append(p).append("\n");
        }

        // ticket con el precio final
        sb.append("--------------------------\n");
        sb.append("TOTAL: ").append(total).append("€");
        return sb.toString();
    }

        // Getters y Setters
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
}
