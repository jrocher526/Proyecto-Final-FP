package ui;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Ventana inicial (Pantalla de Bienvenida) de la aplicación.
 * Actúa como punto de entrada visual para establecer la fecha de la sesión de trabajo
 * antes de cargar el mapa de mesas.
 */
public class VentanaLogin extends Frame {

    /**
     * Constructor que inicializa y dibuja la pantalla de bienvenida.
     */
    public VentanaLogin() {
        setTitle("Bienvenido al Sistema TPV");
        setSize(400, 300);
        setLocationRelativeTo(null); // Centrar ventana
        setLayout(new BorderLayout(10, 10)); // Márgenes

        // Manejador de eventos para detener la Máquina Virtual de Java si el usuario cierra la ventana principal
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        // CABECERA
        Label lblTitulo = new Label("BIENVENIDO AL SISTEMA TPV", Label.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        // PANEL CENTRAL: FORMULARIO DE FECHA
        Panel panelCentro = new Panel(new GridLayout(3, 1, 10, 10));
        panelCentro.add(new Label("FECHA DE SESIÓN (dd/MM/yyyy):", Label.CENTER));

        // El SimpleDateFormat es una utilidad de Java para traducir fechas entre texto y objetos.
        // La 'M' mayúscula es para Mes, la 'm' minúscula sería para minutos.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Inicializamos el campo de texto inyectando la fecha actual del sistema ya formateada.
        TextField txtFecha = new TextField(sdf.format(new Date()));
        txtFecha.setEditable(true); // Permitimos que el gerente pueda cambiarla si necesita abrir una caja atrasada

        Panel pTxt = new Panel();
        pTxt.add(txtFecha);
        panelCentro.add(pTxt);

        add(panelCentro, BorderLayout.CENTER);

        // PANEL INFERIOR: ACCESO AL SISTEMA
        Button btnContinuar = new Button("CONTINUAR --->");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 14));

        btnContinuar.addActionListener(e -> {
            try {
                // Intentamos parsear el String que ha escrito el usuario a un objeto Date real.
                // Si el formato es perfecto, el código avanza a la siguiente línea.
                Date fechaSeleccionada = sdf.parse(txtFecha.getText());

                // Instanciamos la ventana principal del restaurante pasándole la fecha validada
                new VentanaMesas(fechaSeleccionada).setVisible(true);

                // Destruimos esta ventana de login para liberar RAM
                this.dispose();

            } catch (Exception ex) {
                // VALIDACIÓN DE ENTRADA
                // La capturamos aquí para evitar que el programa se cuelgue, avisando al usuario amigablemente.
                MensajesAWT.mostrarMensaje(this, "Formato de fecha incorrecto. Usa dd/MM/yyyy", "Error");
            }
        });

        Panel panelSur = new Panel();
        panelSur.add(btnContinuar);
        add(panelSur, BorderLayout.SOUTH);
    }
}