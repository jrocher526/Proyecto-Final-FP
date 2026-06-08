package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import modelo.Mesa;
import dao.UsuarioDAO;

/**
 * Cuadro de diálogo modal que aparece al pulsar sobre una mesa.
 * Su función es obligar a seleccionar qué camarero va a atender la comanda
 * consultando la lista de empleados activos en la base de datos.
 */
public class DialogoSeleccionCamarero extends Dialog {

    /**
     * Constructor del diálogo.
     * @param parent La ventana principal de la sala (VentanaMesas) sobre la que se dibuja este diálogo.
     * @param mesa El objeto de la mesa seleccionada que se va a abrir.
     */
    public DialogoSeleccionCamarero(Frame parent, Mesa mesa) {
        // Llamamos al constructor de la clase Dialog.
        super(parent, "Selección de Camarero", true);

        setSize(300, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Evento para cerrar la ventana con la x
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        // Cabecera
        Label titulo = new Label("SELECCIÓN DE CAMARERO", Label.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        add(titulo, BorderLayout.NORTH);

        // Conexion a base de datos
        UsuarioDAO uDao = new UsuarioDAO();
        List<String> camareros = uDao.obtenerNombresCamareros();

        // generar botones
        // Usamos Math.max para que el GridLayout tenga al menos 4 filas aunque haya solo 1 camarero.
        // Si hay más de 4, el GridLayout crecerá automáticamente (camareros.size()).
        Panel panelBotones = new Panel(new GridLayout(Math.max(4, camareros.size()), 1, 10, 10));

        if (camareros.isEmpty()) {
            // Si el administrador ha borrado a todos o la BD está vacía
            panelBotones.add(new Label("No hay camareros.", Label.CENTER));
        } else {
            // Bucle for-each que recorre la lista obtenida de la BD
            for (String nombreCamarero : camareros) {
                // Generamos un botón nuevo por cada nombre encontrado
                Button btnCamarero = new Button(nombreCamarero);

                // Le añadimos un evento a cada botón recién creado
                btnCamarero.addActionListener(e -> {
                    // Al pulsar, instanciamos la pantalla de la comanda pasándole la mesa,
                    // el ticket activo y el nombre del botón que acabamos de pulsar
                    new VentanaComanda(mesa, nombreCamarero, mesa.getTicketActivo()).setVisible(true);

                    // cerrar ventanas anteriores
                    parent.dispose();
                    this.dispose();
                });

                // Añadimos el botón al panel
                panelBotones.add(btnCamarero);
            }
        }

        // si hay mas camareros se crea un scroll
        ScrollPane scroll = new ScrollPane();
        scroll.add(panelBotones);
        add(scroll, BorderLayout.CENTER);
    }
}