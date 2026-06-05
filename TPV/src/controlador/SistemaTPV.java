package controlador;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import modelo.Mesa;
import modelo.Ticket;

/**
 * Clase controladora central del programa.
 * Actúa como el "cerebro" de la sesión actual, manteniendo en memoria el estado de todas las mesas
 * y el dinero acumulado en la caja durante el día.
 */
public class SistemaTPV {
    private Date fechaSesion;
    private float totalCaja;
    private int numeroTickets;

    // Usamos un Map (Diccionario) en lugar de un ArrayList o un array normal.
    // La clave (Integer) será el número de la mesa, y el valor (Mesa) será el objeto físico.
    private Map<Integer, Mesa> mapaMesas;

    /**
     * Constructor del Sistema.
     * Al arrancar, inicializa los contadores a cero y prepara la sala física creando las mesas.
     */
    public SistemaTPV() {
        this.fechaSesion = new Date();
        this.totalCaja = 0.0f;
        this.numeroTickets = 0;

        // Instanciamos el HashMap para almacenar las mesas en memoria RAM
        this.mapaMesas = new HashMap<>();

        // Bucle for para crear las 12 mesas del restaurante y meterlas en el mapa
        for (int i = 1; i <= 12; i++) {
            mapaMesas.put(i, new Mesa(i));
        }
    }

    /**
     * Reinicia el sistema para una nueva jornada de trabajo.
     * @param fecha La fecha introducida en la pantalla de Login.
     */
    public void iniciarSesion(Date fecha) {
        this.fechaSesion = fecha;
        this.totalCaja = 0.0f; // Ponemos el contador de dinero a cero al empezar el día
    }

    /**
     * Acumula el dinero de un ticket recién cobrado en la caja general del día.
     * @param t El ticket que acaba de pagar el cliente.
     */
    public void registrarTicket(Ticket t) {
        // comprobamos que el ticket no venga nulo antes de sumarlo
        if (t != null) {
            this.totalCaja += t.getTotal();
            this.numeroTickets++; // Incrementamos el número total de ventas del día
        }
    }

    /**
     * Recupera una mesa específica de la memoria para poder modificarla (ver qué han pedido, cobrar, etc).
     * @param numero El número físico de la mesa que queremos buscar (del 1 al 12).
     * @return El objeto Mesa solicitado.
     */
    public Mesa getMesa(int numero) {
        // El método 'get' del HashMap busca instantáneamente la mesa por su número de clave
        return mapaMesas.get(numero);
    }
}