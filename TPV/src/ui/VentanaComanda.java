package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modelo.Mesa;
import modelo.Ticket;
import modelo.Producto;
import dao.ProductoDAO;

/**
 * Interfaz principal para la toma de pedidos en una mesa.
 * Genera dinámicamente los botones de la carta leyendo de la base de datos
 * y agrupa los productos seleccionados en una lista visual.
 */
public class VentanaComanda extends Frame {

    // COMPONENTES VISUALES Y DE LÓGICA
    // Usamos la ruta completa java.awt.List
    private java.awt.List listaTicketVisual;
    private Label lblTotal;

    private Ticket ticket;
    private Mesa mesa;

    // Esta lista paralela nos permite saber exactamente qué productos
    // corresponden a la línea de texto que el usuario ha seleccionado en la pantalla.
    private List<List<Producto>> lineasVisuales;

    /**
     * Constructor de la ventana de comandas.
     * @param mesa La mesa física a la que estamos atendiendo.
     * @param camarero El nombre del empleado que ha abierto la mesa.
     * @param ticket El objeto ticket donde se irán guardando los productos.
     */
    public VentanaComanda(Mesa mesa, String camarero, Ticket ticket) {
        this.mesa = mesa;
        this.ticket = ticket;
        this.lineasVisuales = new ArrayList<>();

        // Registramos al camarero en el ticket usando un Set para evitar duplicados
        this.ticket.añadirCamarero(camarero);

        setTitle("Mesa Nº " + mesa.getNumero() + " - " + ticket.getNombresCamareros());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Evento para cerrar la ventana desde la 'X' superior
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        //TICKET VISUAL
        Panel panelIzquierdo = new Panel(new BorderLayout());
        panelIzquierdo.setPreferredSize(new Dimension(400, 0));
        panelIzquierdo.add(new Label("PRODUCTOS DEL TICKET", Label.CENTER), BorderLayout.NORTH);

        listaTicketVisual = new java.awt.List();

        listaTicketVisual.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panelIzquierdo.add(listaTicketVisual, BorderLayout.CENTER);
        add(panelIzquierdo, BorderLayout.WEST);

        // PANEL CENTRAL: BOTONES DE LA CARTA
        Panel panelProductos = new Panel(new GridLayout(0, 3, 5, 5));

        // Consultamos la BD para traernos todos los productos disponibles
        ProductoDAO dao = new ProductoDAO();
        List<Producto> carta = dao.obtenerTodos();

        // Generación dinámica de la botonera
        for (Producto p : carta) {
            Button btnProd = new Button(p.getNombre());
            btnProd.addActionListener(e -> {
                // Instanciamos un producto clonado para no machacar la referencia original en memoria
                Producto prodClonado = new Producto(p.getId(), p.getNombre(), p.getCategoria(), p.getPrecio());
                this.ticket.añadirProducto(prodClonado);
                actualizarResumen(); // Refrescamos la pantalla cada vez que pulsamos
            });
            panelProductos.add(btnProd);
        }

        // Metemos los botones en un ScrollPane por si la carta es muy grande y no caben en pantalla
        ScrollPane scrollProds = new ScrollPane();
        scrollProds.add(panelProductos);
        add(scrollProds, BorderLayout.CENTER);

        // PANEL INFERIOR: CONTROLES Y TOTAL
        Panel panelSur = new Panel(new BorderLayout());
        Panel panelControles = new Panel(new FlowLayout(FlowLayout.LEFT));

        Button btnTicket = new Button("TICKET");
        Button btnModificar = new Button("MODIFICAR");
        Button btnCobrar = new Button("COBRAR");
        Button btnSalir = new Button("SALIR");

        // 1. Botón TICKET (Imprimir)
        btnTicket.addActionListener(e -> {
            if (ticket.getProductos().isEmpty()) {
                MensajesAWT.mostrarMensaje(this, "El ticket está vacío.", "Aviso");
                return;
            }
            try {
                // Guardamos el ticket en el Escritorio usando la clase File y el separador del Sistema Operativo
                String nombreFichero = System.getProperty("user.home") + java.io.File.separator + "Desktop" + java.io.File.separator + "Ticket_Mesa_" + mesa.getNumero() + ".txt";
                FileWriter fw = new FileWriter(nombreFichero);
                fw.write(ticket.toString()); // Usamos el método toString() sobreescrito en la clase Ticket
                fw.write("\n¡Gracias por su visita!");
                fw.close();
                MensajesAWT.mostrarMensaje(this, "Ticket impreso.", "Éxito");
            } catch (IOException ex) {
                MensajesAWT.mostrarMensaje(this, "Error al generar el archivo.", "Error");
            }
        });

        // 2. Botón MODIFICAR
        btnModificar.addActionListener(e -> {
            // Restamos 2 porque las posiciones 0 y 1 de la lista visual son las cabeceras de texto ("CANT | PRODUCTO...")
            int filaSel = listaTicketVisual.getSelectedIndex() - 2;

            if (filaSel >= 0 && filaSel < lineasVisuales.size()) {
                // Extraemos la lista de productos idénticos
                List<Producto> productosLinea = lineasVisuales.get(filaSel);
                Producto prodRef = productosLinea.get(0); // Tomamos el primero como molde
                int cantidadActual = productosLinea.size();

                String[] opciones = {"Modificar Cantidad", "Modificar Precio", "Eliminar de la comanda"};
                String eleccion = MensajesAWT.pedirOpcion(this, "¿Qué deseas hacer con " + prodRef.getNombre() + "?", "Opciones", opciones);

                if ("Modificar Cantidad".equals(eleccion)) {
                    String nuevaCantStr = MensajesAWT.pedirInput(this, "Nueva cantidad para " + prodRef.getNombre() + ":", "Cantidad", String.valueOf(cantidadActual));
                    if(nuevaCantStr != null) {
                        try {
                            int cant = Integer.parseInt(nuevaCantStr);
                            if (cant <= 0) { MensajesAWT.mostrarMensaje(this, "Cantidad no válida.", "Error"); return; }

                            // Borramos todos los productos viejos de ese tipo y metemos los nuevos con la cantidad exacta
                            this.ticket.getProductos().removeAll(productosLinea);
                            for (int i = 0; i < cant; i++) {
                                this.ticket.añadirProducto(new Producto(prodRef.getId(), prodRef.getNombre(), prodRef.getCategoria(), prodRef.getPrecio()));
                            }
                            this.ticket.calcularTotal();
                            actualizarResumen();
                        } catch (Exception ex) { MensajesAWT.mostrarMensaje(this, "Número inválido.", "Error"); }
                    }
                } else if ("Modificar Precio".equals(eleccion)) {
                    String nuevoPrecioStr = MensajesAWT.pedirInput(this, "Nuevo precio unitario (€):", "Precio", String.valueOf(prodRef.getPrecio()));
                    if(nuevoPrecioStr != null) {
                        try {
                            float precio = Float.parseFloat(nuevoPrecioStr.replace(",", "."));
                            this.ticket.getProductos().removeAll(productosLinea);
                            for (int i = 0; i < cantidadActual; i++) {
                                this.ticket.añadirProducto(new Producto(prodRef.getId(), prodRef.getNombre(), prodRef.getCategoria(), precio));
                            }
                            this.ticket.calcularTotal();
                            actualizarResumen();
                        } catch (Exception ex) { MensajesAWT.mostrarMensaje(this, "Número inválido.", "Error"); }
                    }
                } else if ("Eliminar de la comanda".equals(eleccion)) {
                    if (MensajesAWT.pedirConfirmacion(this, "¿Seguro que deseas eliminar " + prodRef.getNombre() + " del ticket?", "Eliminar Producto")) {
                        this.ticket.getProductos().removeAll(productosLinea);
                        this.ticket.calcularTotal();
                        actualizarResumen();
                    }
                }
            } else {
                MensajesAWT.mostrarMensaje(this, "Selecciona un producto de la lista primero.", "Atención");
            }
        });

        // 3. Botón SALIR
        btnSalir.addActionListener(e -> {
            new VentanaMesas(new java.util.Date()).setVisible(true);
            this.dispose();
        });

        // 4. Botón COBRAR
        btnCobrar.addActionListener(e -> {
            if(!ticket.getProductos().isEmpty()){
                // Abrimos la ventana de cobro (modal)
                new DialogoCobro(this, mesa, camarero, this.ticket).setVisible(true);
            } else {
                MensajesAWT.mostrarMensaje(this, "No hay productos que cobrar.", "Aviso");
            }
        });

        panelControles.add(btnTicket);
        panelControles.add(btnModificar);
        panelControles.add(btnCobrar);
        panelControles.add(btnSalir);

        lblTotal = new Label("TOTAL: 0.00€  ", Label.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 24));

        panelSur.add(panelControles, BorderLayout.WEST);
        panelSur.add(lblTotal, BorderLayout.EAST);
        add(panelSur, BorderLayout.SOUTH);

        // Llamamos a esto al abrir la ventana para pintar la lista si la mesa ya tenía cosas de antes
        actualizarResumen();
    }

    /**
     * Motor de dibujado del ticket.
     * Agrupa los productos idénticos para que no salgan 5 líneas de "Coca-Cola" separadas,
     * sino una sola línea con "5 x Coca-Cola".
     */
    private void actualizarResumen() {
        listaTicketVisual.removeAll();
        lineasVisuales.clear();

        // Si vaciamos el ticket borrando cosas, la mesa vuelve a estar libre
        if (ticket.getProductos().isEmpty()) { mesa.cambiarEstado(modelo.EstadoMesa.LIBRE); }
        else { mesa.cambiarEstado(modelo.EstadoMesa.OCUPADA); }


        // Usamos un Mapa para agrupar usando como clave "Nombre_Precio".
        // Linked porque mantiene el orden en el que se fueron pidiendo las cosas.
        Map<String, List<Producto>> agrupados = new LinkedHashMap<>();
        for (Producto p : ticket.getProductos()) {
            String clave = p.getNombre() + "_" + p.getPrecio();
            // computeIfAbsent: si no existe la lista para esta clave, la crea. Luego añade el producto.
            agrupados.computeIfAbsent(clave, k -> new ArrayList<>()).add(p);
        }

        // Imprimimos la cabecera formateada
        listaTicketVisual.add(String.format("%-5s | %-15s | %-8s | %-8s", "CANT", "PRODUCTO", "P.UNIT", "TOTAL"));
        listaTicketVisual.add("--------------------------------------------------");

        for (List<Producto> lista : agrupados.values()) {
            // Guardamos la lista en nuestra memoria paralela para poder modificarla después si el usuario hace clic
            lineasVisuales.add(lista);

            int cant = lista.size();
            Producto ref = lista.get(0);

            // String.format:
            // %-5d significa "Número entero, alineado a la izquierda, ocupando 5 caracteres exactos"
            // %-15s significa "String, alineado a la izquierda, ocupando 15 caracteres"
            String textoLinea = String.format("%-5d | %-15s | %-8s | %-8s",
                    cant,
                    ref.getNombre(),
                    String.format("%.2f€", ref.getPrecio()),
                    String.format("%.2f€", cant * ref.getPrecio()));

            listaTicketVisual.add(textoLinea);
        }

        lblTotal.setText("TOTAL: " + String.format("%.2f", ticket.getTotal()) + "€  ");
    }
}