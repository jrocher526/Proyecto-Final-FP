package ui;

import java.awt.*;
import java.awt.event.*;
import modelo.Mesa;
import modelo.Ticket;
import modelo.EstadoMesa;
import dao.TicketDAO;
import dao.TicketObjectDBDAO;

/**
 * Cuadro de diálogo modal que gestiona el proceso de cobro de una mesa.
 */
public class DialogoCobro extends Dialog {
    /**
     * Constructor del diálogo de cobro.
     * @param parent La ventana principal que invoca este diálogo (para poder bloquearla).
     * @param mesa El objeto Mesa físico que se va a cobrar.
     * @param camarero El nombre del empleado que está cerrando la cuenta.
     * @param ticketActivo El ticket con todos los productos y el dinero a pagar.
     */
    public DialogoCobro(Frame parent, Mesa mesa, String camarero, Ticket ticketActivo) {
        // Esto impide que el usuario pueda hacer clic en la ventana de atrás hasta que termine de cobrar.
        super(parent, "Cobro", true);

        setSize(400, 300);
        setLocationRelativeTo(parent); //centrar ventana encima de la de atras
        setLayout(new BorderLayout()); // Usamos BorderLayout para poner el resumen al centro y el botón abajo

        // cerrar con la x la ventana
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        // Resumen de la cuenta:
        Panel panelCentro = new Panel(new GridLayout(3, 1));

        Label lblInfo = new Label("MESA Nº " + mesa.getNumero() + " | " + ticketActivo.getNombresCamareros(), Label.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 14));

        Label lblResumen = new Label(ticketActivo.getProductos().size() + " productos consumidos", Label.CENTER);

        // Formateamos el float a 2 decimales para que se vea como dinero
        Label lblMonto = new Label("MONTO COBRADO: " + String.format("%.2f", ticketActivo.getTotal()) + "€", Label.CENTER);
        lblMonto.setFont(new Font("Arial", Font.BOLD, 20));
        lblMonto.setForeground(Color.RED);

        panelCentro.add(lblInfo);
        panelCentro.add(lblResumen);
        panelCentro.add(lblMonto);
        add(panelCentro, BorderLayout.CENTER);

        // cobrar y persistencia
        Button btnRegresar = new Button("ACEPTAR PAGO Y REGRESAR");
        btnRegresar.addActionListener(e -> {
            try {
                // bloquear mesa hasta el cobro
                mesa.cambiarEstado(EstadoMesa.PENDIENTE_PAGO);
                ticketActivo.cobrar();

                // guardar los datos básicos contables en la base de datos relacional (MariaDB)
                new TicketDAO().guardarTicket(ticketActivo);

                // PA para guardar el objeto en la BD de objetos
                new TicketObjectDBDAO().guardarTicketObjeto(ticketActivo);

                // resetear la mesa
                // liberarla en estado PENDIENTE_PAGO, la pasamos a OCUPADA un instante para poder vaciarla.
                mesa.cambiarEstado(EstadoMesa.OCUPADA);
                mesa.liberarMesa();

            } catch (Exception ex) {
                MensajesAWT.mostrarMensaje(parent, "Error en BD: " + ex.getMessage(), "Error");
            }

            // recargar la interfaz gráfica volviendo a instanciar la pantalla de las mesas
            new VentanaMesas(new java.util.Date()).setVisible(true);

            // disposepara las ventanas antiguas de la memoria RAM para evitar fugas de memoria
            parent.dispose();
            this.dispose();
        });

        add(btnRegresar, BorderLayout.SOUTH);
    }
}