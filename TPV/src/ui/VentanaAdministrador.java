package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import modelo.Producto;
import modelo.Categoria;
import modelo.Ticket;
import dao.ProductoDAO;
import dao.UsuarioDAO;
import dao.TicketObjectDBDAO;

/**
 * Ventana del Panel de Control exclusivo para el Administrador.
 * Desde aquí se gestiona el CRUD de productos, altas/bajas de empleados y el cierre de caja.
 */
public class VentanaAdministrador extends Frame {

    // Instanciamos los DAOs para poder hablar con las bases de datos
    private ProductoDAO productoDAO = new ProductoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Constructor que dibuja toda la interfaz de administración.
     */
    public VentanaAdministrador() {
        setTitle("Panel de Control - Administrador");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Evento para cerrar el programa desde la X
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        Label lblTitulo = new Label("GESTIÓN DEL RESTAURANTE", Label.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        add(lblTitulo, BorderLayout.NORTH);

        // Cuadrícula para colocar los botones de gestión bien ordenados
        Panel panelBotones = new Panel(new GridLayout(2, 3, 20, 20));

        Button btnAñadirProd = crearBoton("Añadir Producto", Color.decode("#4CAF50"));
        Button btnGestionarProd = crearBoton("Modificar Producto", Color.decode("#FF9800"));
        Button btnCierreCaja = crearBoton("CIERRE DE CAJA", Color.decode("#607D8B"));
        Button btnAñadirCam = crearBoton("Añadir Camarero", Color.decode("#2196F3"));
        Button btnEliminarCam = crearBoton("Eliminar Camarero", Color.decode("#9C27B0"));

        // --- EVENTOS DE LOS BOTONES ---

        btnAñadirProd.addActionListener(e -> {
            Dialog d = new Dialog(this, "Nuevo Producto", true);
            d.setLayout(new GridLayout(4, 2));
            d.add(new Label("Nombre:")); TextField txtNombre = new TextField(); d.add(txtNombre);
            d.add(new Label("Categoría:")); Choice cbCat = new Choice();
            cbCat.add("BEBIDA"); cbCat.add("COMIDA"); cbCat.add("POSTRE"); d.add(cbCat);
            d.add(new Label("Precio (€):")); TextField txtPrecio = new TextField(); d.add(txtPrecio);

            Button btnOk = new Button("Guardar");
            btnOk.addActionListener(ev -> {
                try {
                    // Sustituimos la coma por punto por si el usuario escribe "1,50" en vez de "1.50"
                    float precio = Float.parseFloat(txtPrecio.getText().replace(",", "."));
                    productoDAO.insertarProducto(new Producto(0, txtNombre.getText().toUpperCase(), Categoria.valueOf(cbCat.getSelectedItem()), precio));
                    MensajesAWT.mostrarMensaje(this, "Producto añadido.", "Éxito");
                    d.dispose();
                } catch(Exception ex) { MensajesAWT.mostrarMensaje(this, "Datos inválidos", "Error"); }
            });
            Button btnCancel = new Button("Cancelar");
            btnCancel.addActionListener(ev -> d.dispose());
            d.add(btnOk); d.add(btnCancel);
            d.setSize(300, 200); d.setLocationRelativeTo(this);
            d.setVisible(true);
        });

        btnGestionarProd.addActionListener(e -> {
            List<Producto> lista = productoDAO.obtenerTodos();
            if (lista.isEmpty()) return;
            // Usamos Streams para extraer solo los nombres de los productos y pasarlos al menú
            String[] nombres = lista.stream().map(Producto::getNombre).toArray(String[]::new);
            String seleccionado = MensajesAWT.pedirOpcion(this, "Selecciona el producto:", "Gestión", nombres);

            if (seleccionado != null) {
                Producto p = lista.stream().filter(prod -> prod.getNombre().equals(seleccionado)).findFirst().orElse(null);
                String[] opciones = {"Modificar Nombre", "Modificar Precio", "Eliminar Producto"};
                String eleccion = MensajesAWT.pedirOpcion(this, "Acción para: " + seleccionado, "Opciones", opciones);

                if ("Modificar Nombre".equals(eleccion)) {
                    String nName = MensajesAWT.pedirInput(this, "Nuevo nombre:", "Renombrar", p.getNombre());
                    if (nName != null && !nName.trim().isEmpty()) {
                        productoDAO.modificarNombreProducto(seleccionado, nName.toUpperCase());
                        MensajesAWT.mostrarMensaje(this, "Nombre actualizado.", "Info");
                    }
                } else if ("Modificar Precio".equals(eleccion)) {
                    String nPrecio = MensajesAWT.pedirInput(this, "Nuevo precio (€):", "Precio", String.valueOf(p.getPrecio()));
                    if (nPrecio != null) {
                        try {
                            productoDAO.modificarProducto(seleccionado, Float.parseFloat(nPrecio.replace(",", ".")));
                            MensajesAWT.mostrarMensaje(this, "Precio actualizado.", "Info");
                        } catch(Exception ex) { MensajesAWT.mostrarMensaje(this, "Formato incorrecto", "Error"); }
                    }
                } else if ("Eliminar Producto".equals(eleccion)) {
                    if (MensajesAWT.pedirConfirmacion(this, "¿Seguro que quieres eliminarlo?", "Eliminar")) {
                        productoDAO.eliminarProducto(seleccionado);
                        MensajesAWT.mostrarMensaje(this, "Producto eliminado.", "Info");
                    }
                }
            }
        });

        btnAñadirCam.addActionListener(e -> {
            // En lugar de usar pedirInput genérico, creamos un Dialog manual para controlar el tamaño
            Dialog d = new Dialog(this, "Añadir Nuevo Camarero", true);
            d.setLayout(new BorderLayout(10, 10));

            Label lblAviso = new Label("Introduce el nombre del nuevo camarero:", Label.CENTER);
            lblAviso.setFont(new Font("Arial", Font.BOLD, 16));
            d.add(lblAviso, BorderLayout.NORTH);

            // Creamos un TextField y le aumentamos la fuente a 24.
            TextField txtNombre = new TextField(15);
            txtNombre.setFont(new Font("Arial", Font.PLAIN, 24));

            Panel pCentro = new Panel();
            pCentro.add(txtNombre);
            d.add(pCentro, BorderLayout.CENTER);

            Panel pBotones = new Panel();
            Button btnOk = new Button("Guardar");
            btnOk.setFont(new Font("Arial", Font.BOLD, 14));
            Button btnCancel = new Button("Cancelar");
            btnCancel.setFont(new Font("Arial", Font.BOLD, 14));

            btnOk.addActionListener(ev -> {
                String nombre = txtNombre.getText();
                if (nombre != null && !nombre.trim().isEmpty()) {
                    usuarioDAO.insertarCamarero(nombre.toUpperCase());
                    MensajesAWT.mostrarMensaje(this, "Camarero añadido con éxito.", "Info");
                    d.dispose();
                } else {
                    MensajesAWT.mostrarMensaje(this, "El nombre no puede estar vacío.", "Error");
                }
            });

            btnCancel.addActionListener(ev -> d.dispose());

            pBotones.add(btnOk);
            pBotones.add(btnCancel);
            d.add(pBotones, BorderLayout.SOUTH);

            d.setSize(400, 200);
            d.setLocationRelativeTo(this);
            d.setVisible(true);
        });

        btnEliminarCam.addActionListener(e -> {
            List<String> cams = usuarioDAO.obtenerNombresCamareros();
            if(!cams.isEmpty()){
                String sel = MensajesAWT.pedirOpcion(this, "Selecciona camarero:", "Eliminar", cams.toArray(new String[0]));
                if (sel != null && MensajesAWT.pedirConfirmacion(this, "¿Eliminar a " + sel + "?", "Confirmar")) {
                    usuarioDAO.eliminarCamarero(sel);
                    MensajesAWT.mostrarMensaje(this, "Camarero eliminado.", "Info");
                }
            }
        });

        // Llamamos al método especializado en hacer la contabilidad y crear las carpetas
        btnCierreCaja.addActionListener(e -> realizarCierreDeCaja());

        panelBotones.add(btnAñadirProd); panelBotones.add(btnGestionarProd); panelBotones.add(btnCierreCaja);
        panelBotones.add(btnAñadirCam); panelBotones.add(btnEliminarCam);
        add(panelBotones, BorderLayout.CENTER);

        Button btnSalir = new Button("VOLVER A SALA");
        btnSalir.addActionListener(e -> { new VentanaMesas(new Date()).setVisible(true); this.dispose(); });
        Panel pSur = new Panel(); pSur.add(btnSalir);
        add(pSur, BorderLayout.SOUTH);
    }

    /**
     * Método auxiliar para no repetir código al configurar el diseño de los botones.
     */
    private Button crearBoton(String texto, Color color) {
        Button btn = new Button(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        return btn;
    }

    /**
     * Calcula el dinero total del día basándose en los tickets de ObjectDB,
     * crea la estructura de carpetas por mes y guarda el archivo de texto.
     */
    private void realizarCierreDeCaja() {
        TicketObjectDBDAO tDao = new TicketObjectDBDAO();
        List<Ticket> ticketsHoy = tDao.obtenerTicketsHoy();

        if (ticketsHoy.isEmpty()) {
            MensajesAWT.mostrarMensaje(this, "No hay tickets hoy.", "Cierre");
            return;
        }

        float totalCaja = 0;
        for (Ticket t : ticketsHoy) {
            totalCaja += t.getTotal();
        }

        // --- SISTEMA DINÁMICO DE CARPETAS ---
        Date fechaActual = new Date();
        String mesAnio = new SimpleDateFormat("MM_yyyy").format(fechaActual);
        String fechaDia = new SimpleDateFormat("dd-MM-yyyy").format(fechaActual);

        String rutaEscritorio = System.getProperty("user.home") + File.separator + "Desktop";
        File carpetaPrincipal = new File(rutaEscritorio, "ResumenCaja");
        File carpetaMes = new File(carpetaPrincipal, "Mes_" + mesAnio);

        if (!carpetaMes.exists()) {
            carpetaMes.mkdirs();
        }

        File archivoTXT = new File(carpetaMes, "Cierre_" + fechaDia + ".txt");

        try (FileWriter fw = new FileWriter(archivoTXT, true)) {
            String horaExacta = new SimpleDateFormat("HH:mm:ss").format(fechaActual);
            fw.write("=======================================\n");
            fw.write("CIERRE DE CAJA - " + fechaDia + " a las " + horaExacta + "\n");
            fw.write("TOTAL FACTURADO: " + String.format("%.2f", totalCaja) + "€\n");
            fw.write("Tickets procesados: " + ticketsHoy.size() + "\n");
            fw.write("=======================================\n\n");
        } catch (Exception ex) {
            MensajesAWT.mostrarMensaje(this, "Error al crear las carpetas o el archivo.", "Error I/O");
            return;
        }

        // --- NUEVA VENTANA DE RESUMEN MÁS GRANDE ---
        Dialog dResumen = new Dialog(this, "Cierre Finalizado", true);
        dResumen.setLayout(new BorderLayout(10, 10));

        // Preparamos el texto con los saltos de línea
        String textoCierre = "TOTAL FACTURADO HOY: " + String.format("%.2f", totalCaja) + "€\n\n"
                + "Los datos se han guardado correctamente en tu ordenador.\n"
                + "Ruta: Escritorio / ResumenCaja / Mes_" + mesAnio;

        // Usamos un TextArea para que admita varias líneas de texto sin cortarse
        TextArea txtResumen = new TextArea(textoCierre, 5, 40, TextArea.SCROLLBARS_NONE);
        txtResumen.setEditable(false); // Para que no se pueda borrar el texto por error
        txtResumen.setFont(new Font("Arial", Font.BOLD, 14));
        txtResumen.setBackground(SystemColor.control); // Le ponemos color de fondo estándar de ventana

        // Añadimos márgenes invisibles (Paneles vacíos) para que no quede pegado a los bordes
        dResumen.add(new Panel(), BorderLayout.NORTH);
        dResumen.add(new Panel(), BorderLayout.WEST);
        dResumen.add(new Panel(), BorderLayout.EAST);
        dResumen.add(txtResumen, BorderLayout.CENTER);

        // Botón de Aceptar
        Button btnAceptar = new Button("Aceptar");
        btnAceptar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAceptar.addActionListener(ev -> dResumen.dispose());

        Panel pBotones = new Panel();
        pBotones.add(btnAceptar);
        dResumen.add(pBotones, BorderLayout.SOUTH);

        // tamaño de ventana
        dResumen.setSize(500, 220);
        dResumen.setLocationRelativeTo(this);
        dResumen.setVisible(true);
    }
}