package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

/**
 * Representa la cuenta completa de una mesa.
 * Es la entidad principal que vamos a guardar en la base de datos orientada a objetos (ObjectDB).
 */
@Entity
public class Ticket implements Serializable {

    // Se usa Serializable para evitar problemas de compatibilidad si modificamos la clase en el futuro
    private static final long serialVersionUID = 1L;

    @Id // Indica que este es el campo Clave Primaria
    @GeneratedValue // Le dice a la BD que genere este número automáticamente (como un AUTO_INCREMENT)
    private Long idBD;

    // ATRIBUTOS DEL TICKET
    private int numeroTicket;

    // Inicializamos las listas
    private ArrayList<Producto> productos = new ArrayList<>();
    private float total;
    private String observaciones;
    private Date fecha;

    // Usamos un HashSet porque no permite duplicados
    // Así, si el camarero David  entra 5 veces a modificar la mesa, solo se guarda una vez su nombre.
    private HashSet<String> camareros = new HashSet<>();

    /**
     * Constructor vacío.
     * Es obligatorio ponerlo porque JPA (ObjectDB) lo necesita para poder
     * reconstruir los objetos cuando los lee de la base de datos.
     */
    public Ticket() {}

    /**
     * Constructor que usamos en nuestro programa al abrir una mesa nueva.
     * @param numeroTicket El número de la mesa a la que se asocia la cuenta.
     */
    public Ticket(int numeroTicket) {
        this.numeroTicket = numeroTicket;
        this.total = 0.0f;
        this.observaciones = "";
        this.fecha = new Date();
    }

    //GETTERS Y SETTERS

    public int getNumeroTicket() {
        return numeroTicket;
    }
    public ArrayList<Producto> getProductos() {
        return productos;
    }
    public float getTotal() {
        return total;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public Date getFecha() {
        return fecha;
    }


    /**
     * Registra un camarero en el ticket asegurando que la colección exista.
     * @param nombre Nombre del camarero.
     */
    public void añadirCamarero(String nombre) {
        if (this.camareros == null) {
            this.camareros = new HashSet<>();
        }
        // Solo lo añadimos si el nombre no es nulo y no está en blanco
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.camareros.add(nombre);
        }
    }

    /**
     * Genera un texto con todos los camareros separados por comas.
     * @return String con los nombres, o "Ninguno" si está vacío.
     */
    public String getNombresCamareros() {
        if (this.camareros == null || this.camareros.isEmpty()) {
            return "Ninguno";
        }
        // Convierte el HashSet en un String
        return String.join(", ", this.camareros);
    }

    /**
     * Añade un producto a la comanda y recalcula el dinero total automáticamente.
     * @param p El producto a añadir (ej: una cerveza).
     */
    public void añadirProducto(Producto p) {
        if (p != null) {
            // Medida de seguridad por si la BD nos devolvió la lista en null
            if (this.productos == null) this.productos = new ArrayList<>();

            productos.add(p);
            calcularTotal(); // Mantenemos el precio siempre actualizado
        }
    }

    /**
     * Suma los precios de todos los productos de la lista.
     * @return El precio total del ticket.
     */
    public float calcularTotal() {
        if (this.productos == null) return 0.0f;

        // Usamos la API de Streams de Java 8.
        // Coge la lista, extrae solo los precios (mapToDouble) y los suma todos (sum) de golpe.
        this.total = (float) productos.stream().mapToDouble(Producto::getPrecio).sum();
        return this.total;
    }

    /**
     * Simula el cobro en el sistema.
     */
    public void cobrar() {
        // En una app real aquí podríamos mandar la orden a la impresora térmica
        System.out.println("Ticket Nº " + numeroTicket + " cobrado.");
    }

    /**
     * Prepara el diseño gráfico del ticket en texto plano para guardarlo en archivos .txt
     */
    @Override
    public String toString() {
        // Usamos StringBuilder porque concatenar textos con el símbolo '+' dentro de un bucle
        // es muy ineficiente y consume mucha memoria. StringBuilder es la forma mas facil  de hacerlo.
        StringBuilder sb = new StringBuilder();

        sb.append("=== TICKET MESA Nº ").append(numeroTicket).append(" ===\n");
        sb.append("ATENDIDO POR: ").append(getNombresCamareros()).append("\n");
        sb.append("--------------------------------\n");

        if (productos != null) {
            // lambda para añadir una línea de texto por cada producto consumido
            productos.forEach(p ->
                    sb.append("- ")
                            .append(p.getNombre())
                            .append(" : ")
                            .append(String.format("%.2f", p.getPrecio()))
                            .append("€\n")
            );
        }

        sb.append("---------------------\nTOTAL: ").append(String.format("%.2f", total)).append("€\n");

        return sb.toString(); // Convertimos el constructor de textos a un String normal y lo devolvemos
    }
}