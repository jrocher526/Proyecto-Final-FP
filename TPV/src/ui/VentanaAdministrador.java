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
 * Ventana principal del Panel de Control exclusivo para el Administrador.
 * Desde aquí se gestiona el CRUD (Crear, Leer, Actualizar, Borrar) de productos y empleados,
 * y se realiza la persistencia en disco del cierre de caja diario.
 */
public class VentanaAdministrador extends Frame {

    // CONEXIONES A BASE DE DATOS
    // Instanciamos los DAOs como atributos de la clase para poder usarlos en todos los botones
    private ProductoDAO productoDAO = new ProductoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Constructor que dibuja y configura toda la interfaz gráfica de administración.
     */
    public VentanaAdministrador() {
        setTitle("Panel de Control - Administrador");
        setSize(800, 500);
        setLocationRelativeTo(null); // centrar ventana
        setLayout(new BorderLayout(10, 10)); // Usamos BorderLayout para poner título arriba y botones al centro

        // evento para cerrar la ventana usando la x
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        // CABECERA
        Label lblTitulo = new Label("GESTIÓN DEL RESTAURANTE", Label.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        add(lblTitulo, BorderLayout.NORTH);

        // PANEL CENTRAL DE BOTONES
        // Cuadrícula dinámica: 2 filas y 3 columnas, con 20 píxeles de separación
        Panel panelBotones = new Panel(new GridLayout(2, 3, 20, 20));

        // Utilizamos un método auxiliar (crearBoton) para no repetir las líneas de colores y fuentes 5 veces
        Button btnAñadirProd = crearBoton("Añadir Producto", Color.decode("#4CAF50"));
        Button btnGestionarProd = crearBoton("Modificar Producto", Color.decode("#FF9800"));
        Button btnCierreCaja = crearBoton("CIERRE DE CAJA", Color.decode("#607D8B"));
        Button btnAñadirCam = crearBoton("Añadir Camarero", Color.decode("#2196F3"));
        Button btnEliminarCam = crearBoton("Eliminar Camarero", Color.decode("#9C27B0"));


        //Evento de los botones

        // AÑADIR PRODUCTO
        btnAñadirProd.addActionListener(e -> {
            Dialog d = new Dialog(this, "Nuevo Producto", true);
            d.setLayout(new GridLayout(4, 2));

            d.add(new Label("Nombre:")); TextField txtNombre = new TextField(); d.add(txtNombre);
            d.add(new Label("Categoría:"));
            Choice cbCat = new Choice(); // desplegable
            cbCat.add("BEBIDA"); cbCat.add("COMIDA"); cbCat.add("POSTRE"); d.add(cbCat);
            d.add(new Label("Precio (€):")); TextField txtPrecio = new TextField(); d.add(txtPrecio);

            Button btnOk = new Button("Guardar");
            btnOk.addActionListener(ev -> {
                try {
                    float precio = Float.parseFloat(txtPrecio.getText().replace(",", "."));

                    // Insertamos pasando todo a Mayúsculas para mantener la base de datos limpia y ordenada
                    productoDAO.insertarProducto(new Producto(0, txtNombre.getText().toUpperCase(), Categoria.valueOf(cbCat.getSelectedItem()), precio));
                    MensajesAWT.mostrarMensaje(this, "Producto añadido.", "Éxito");
                    d.dispose();
                } catch(Exception ex) {
                    // Si el usuario escribe letras en el precio, capturamos el fallo aquí
                    MensajesAWT.mostrarMensaje(this, "Datos inválidos", "Error");
                }
            });
            Button btnCancel = new Button("Cancelar");
            btnCancel.addActionListener(ev -> d.dispose());

            d.add(btnOk); d.add(btnCancel);
            d.setSize(300, 200); d.setLocationRelativeTo(this);
            d.setVisible(true);
        });

        // GESTIONAR PRODUCTO
        btnGestionarProd.addActionListener(e -> {
            List<Producto> lista = productoDAO.obtenerTodos();
            if (lista.isEmpty()) return;

            // Convertimos una lista entera de Objetos Producto en un simple Array de Strings con sus nombres
            String[] nombres = lista.stream().map(Producto::getNombre).toArray(String[]::new);
            String seleccionado = MensajesAWT.pedirOpcion(this, "Selecciona el producto:", "Gestión", nombres);

            if (seleccionado != null) {
                // Volvemos a usar Streams para buscar en la lista original el producto exacto que coincide con el nombre seleccionado
                Producto p = lista.stream().filter(prod -> prod.getNombre().equals(seleccionado)).findFirst().orElse(null);

                String[] opciones = {"Modificar Nombre", "Modificar Precio", "Eliminar Producto"};
                String eleccion = MensajesAWT.pedirOpcion(this, "Acción para: " + seleccionado, "Opciones", opciones);

                // Dependiendo de lo que elija el admin, lanzamos una actualización u otra
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

        // AÑADIR CAMARERO
        btnAñadirCam.addActionListener(e -> {
            Dialog d = new Dialog(this, "Añadir Nuevo Camarero", true);
            d.setLayout(new BorderLayout(10, 10));

            Label lblAviso = new Label("Introduce el nombre del nuevo camarero:", Label.CENTER);
            lblAviso.setFont(new Font("Arial", Font.BOLD, 16));
            d.add(lblAviso, BorderLayout.NORTH);

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
                // Verificamos que el nombre no sea nulo ni un espacio en blanco
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

        // ELIMINAR CAMARERO
        btnEliminarCam.addActionListener(e -> {
            List<String> cams = usuarioDAO.obtenerNombresCamareros();
            if(!cams.isEmpty()){
                String sel = MensajesAWT.pedirOpcion(this, "Selecciona camarero:", "Eliminar", cams.toArray(new String[0]));
                // Pedimos doble confirmación por seguridad antes de borrar un empleado de la BD
                if (sel != null && MensajesAWT.pedirConfirmacion(this, "¿Eliminar a " + sel + "?", "Confirmar")) {
                    usuarioDAO.eliminarCamarero(sel);
                    MensajesAWT.mostrarMensaje(this, "Camarero eliminado.", "Info");
                }
            }
        });

        // CIERRE DE CAJA
        // Delegamos todo el código pesado de guardado de archivos a un método privado para mantener el constructor limpio
        btnCierreCaja.addActionListener(e -> realizarCierreDeCaja());

        // Añadimos todos los botones a su cuadrícula central
        panelBotones.add(btnAñadirProd); panelBotones.add(btnGestionarProd); panelBotones.add(btnCierreCaja);
        panelBotones.add(btnAñadirCam); panelBotones.add(btnEliminarCam);
        add(panelBotones, BorderLayout.CENTER);

        // BOTÓN DE SALIDA
        Button btnSalir = new Button("VOLVER A SALA");
        btnSalir.addActionListener(e -> {
            new VentanaMesas(new Date()).setVisible(true);
            this.dispose(); // Destruimos la ventana de admin y volvemos al panel de mesas
        });
        Panel pSur = new Panel(); pSur.add(btnSalir);
        add(pSur, BorderLayout.SOUTH);
    }

    /**
     * Método auxiliar (Refactorización) para no repetir código al configurar el diseño de los botones.
     */
    private Button crearBoton(String texto, Color color) {
        Button btn = new Button(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE); // Texto blanco para que contraste con los colores vivos del fondo
        return btn;
    }

    /**
     * Extrae todos los tickets de ObjectDB, suma la facturación, crea dinámicamente un árbol de carpetas
     * en el escritorio organizado por meses y guarda un reporte en formato .txt.
     */
    private void realizarCierreDeCaja() {
        // Conectamos con ObjectDB para traernos todos los objetos Ticket
        TicketObjectDBDAO tDao = new TicketObjectDBDAO();
        List<Ticket> ticketsHoy = tDao.obtenerTicketsHoy();

        if (ticketsHoy.isEmpty()) {
            MensajesAWT.mostrarMensaje(this, "No hay tickets hoy.", "Cierre");
            return;
        }

        // Bucle for-each  para sumar los ingresos de todos los tickets
        float totalCaja = 0;
        for (Ticket t : ticketsHoy) {
            totalCaja += t.getTotal();
        }

        // SISTEMA DINÁMICO DE CARPETAS
        Date fechaActual = new Date();

        // Extraemos solo el mes y el año para la carpeta
        String mesAnio = new SimpleDateFormat("MM_yyyy").format(fechaActual);

        // Extraemos la fecha completa para el nombre del archivo de texto
        String fechaDia = new SimpleDateFormat("dd-MM-yyyy").format(fechaActual);

        // System.getProperty("user.home") detecta la ruta del usuario, independientemente de si es Windows, Mac o Linux
        String rutaEscritorio = System.getProperty("user.home") + File.separator + "Desktop";

        // Configuramos los objetos File (No crean la carpeta todavía, solo preparan la ruta en memoria)
        File carpetaPrincipal = new File(rutaEscritorio, "ResumenCaja");
        File carpetaMes = new File(carpetaPrincipal, "Mes_" + mesAnio);

        // Si el árbol de carpetas no existe físicamente en el disco duro, .mkdirs() crea TODA la estructura de golpe
        if (!carpetaMes.exists()) {
            carpetaMes.mkdirs();
        }

        // Preparamos el archivo final dentro de la carpeta del mes
        File archivoTXT = new File(carpetaMes, "Cierre_" + fechaDia + ".txt");

        // Usamos try-with-resources. El parámetro 'true' en FileWriter significa modo "Append" (añadir al final).
        // Si cierro la caja a las 14:00 y luego otra vez a las 23:00, no borra el archivo, lo escribe debajo.
        try (FileWriter fw = new FileWriter(archivoTXT, true)) {
            String horaExacta = new SimpleDateFormat("HH:mm:ss").format(fechaActual);
            fw.write("=======================================\n");
            fw.write("CIERRE DE CAJA - " + fechaDia + " a las " + horaExacta + "\n");
            fw.write("TOTAL FACTURADO: " + String.format("%.2f", totalCaja) + "€\n");
            fw.write("Tickets procesados: " + ticketsHoy.size() + "\n");
            fw.write("=======================================\n\n");
        } catch (Exception ex) {
            MensajesAWT.mostrarMensaje(this, "Error al crear las carpetas o el archivo.", "Error I/O");
            return; // Cortamos la ejecución si no se puede escribir en el disco
        }

        // venatana de resumen
        Dialog dResumen = new Dialog(this, "Cierre Finalizado", true);
        dResumen.setLayout(new BorderLayout(10, 10));

        // Preparamos el bloque de texto con saltos de línea (\n)
        String textoCierre = "TOTAL FACTURADO HOY: " + String.format("%.2f", totalCaja) + "€\n\n"
                + "Los datos se han guardado correctamente en tu ordenador.\n"
                + "Ruta: Escritorio / ResumenCaja / Mes_" + mesAnio;

        // Usamos TextArea en lugar de Label porque Label corta el texto si es muy largo.
        // TextArea soporta texto multilínea sin problemas de formato.
        TextArea txtResumen = new TextArea(textoCierre, 5, 40, TextArea.SCROLLBARS_NONE);

        // Lo ponemos en 'false' para que actúe como un texto de lectura y el usuario no pueda borrarlo por accidente
        txtResumen.setEditable(false);
        txtResumen.setFont(new Font("Arial", Font.BOLD, 14));
        txtResumen.setBackground(SystemColor.control); // Camuflamos el fondo blanco para que parezca una ventana normal

        // Añadimos márgenes invisibles alrededor (Paneles vacíos) para que respire el diseño
        dResumen.add(new Panel(), BorderLayout.NORTH);
        dResumen.add(new Panel(), BorderLayout.WEST);
        dResumen.add(new Panel(), BorderLayout.EAST);
        dResumen.add(txtResumen, BorderLayout.CENTER);

        // Botón para cerrar
        Button btnAceptar = new Button("Aceptar");
        btnAceptar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAceptar.addActionListener(ev -> dResumen.dispose());

        Panel pBotones = new Panel();
        pBotones.add(btnAceptar);
        dResumen.add(pBotones, BorderLayout.SOUTH);

        // ajustar ancho para que se vea todo el textp
        dResumen.setSize(500, 220);
        dResumen.setLocationRelativeTo(this);
        dResumen.setVisible(true);
    }
}