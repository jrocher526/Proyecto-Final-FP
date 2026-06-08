package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import modelo.Mesa;
import modelo.Usuario;
import modelo.EstadoMesa;
import dao.UsuarioDAO;
import controlador.GestorMesas;

/**
 * Ventana principal que representa el plano de la sala del restaurante.
 * Muestra una cuadrícula con todas las mesas disponibles y su estado en tiempo real (por colores),
 * además de proporcionar el acceso restringido al panel de administración.
 */
public class VentanaMesas extends Frame {

    /**
     * Constructor de la vista de la sala.
     * @param fechaSesion La fecha con la que se ha iniciado el turno (viene de VentanaLogin).
     */
    public VentanaMesas(Date fechaSesion) {
        setTitle("Sistema TPV - Sala");
        setSize(800, 600);
        setLocationRelativeTo(null); // Centra la ventana
        setLayout(new BorderLayout());

        // cerrar con la x completamente
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        // PANEL CENTRAL
        // GridLayout de 3 filas y 4 columnas
        // hgap y vgap son la separacion
        Panel panelCuadricula = new Panel(new GridLayout(3, 4, 15, 15));

        //  BOTÓN DE ADMINISTRADOR
        Button btnAdmin = new Button("ADMIN");
        btnAdmin.setBackground(Color.DARK_GRAY);
        btnAdmin.setForeground(Color.WHITE);
        btnAdmin.setFont(new Font("Arial", Font.BOLD, 14));

        btnAdmin.addActionListener(e -> {
            // Usamos nuestra clase de utilidad para pedir la contraseña camuflada con asteriscos
            String pass = MensajesAWT.pedirPassword(this, "Introduzca la contraseña:");

            if (pass != null) {
                // Instanciamos el DAO y enviamos la consulta a MariaDB para comprobar la clave
                UsuarioDAO dao = new UsuarioDAO();
                Usuario admin = dao.validarLogin("admin", pass);

                // Si la consulta devuelve un objeto, el login es correcto
                if (admin != null) {
                    new VentanaAdministrador().setVisible(true);
                    this.dispose(); // Cerramos la sala para abrir el panel de control
                } else {
                    MensajesAWT.mostrarMensaje(this, "Contraseña incorrecta.", "Error");
                }
            }
        });

        // Añadimos el botón de admin en la primera posición de la cuadrícula
        panelCuadricula.add(btnAdmin);

        // Generacion de mesas
        // Bucle que crea las 11 mesas restantes para rellenar los 12 huecos exactos del GridLayout
        for (int i = 1; i <= 11; i++) {

            // Recuperamos la mesa de la memoria RAM usando el patrón Singleton.
            // Esto garantiza que si la mesa 3 estaba roja, siga estando roja al volver a esta ventana.
            Mesa mesaModelo = GestorMesas.getInstancia().getMesa(i);

            Button btnMesa = new Button("Mesa " + i);
            btnMesa.setFont(new Font("Arial", Font.BOLD, 14));

            // Color segun como este la mesa
            if (mesaModelo.getEstado() == EstadoMesa.LIBRE) {
                // Color azul claro (#87CEFA) si la mesa está vacía y lista para usar
                btnMesa.setBackground(Color.decode("#87CEFA"));
                btnMesa.setForeground(Color.BLACK);
            } else {
                // Color rojo carmesí (#D32F2F) si la mesa está OCUPADA o PENDIENTE_PAGO
                btnMesa.setBackground(Color.decode("#D32F2F"));
                btnMesa.setForeground(Color.WHITE);
            }

            // Evento al pulsar sobre una mesa
            btnMesa.addActionListener(e -> {
                // Abrimos el diálogo modal para que el empleado se identifique antes de abrir la comanda
                DialogoSeleccionCamarero d = new DialogoSeleccionCamarero(this, mesaModelo);
                d.setVisible(true);
            });

            panelCuadricula.add(btnMesa);
        }

        // MÁRGENES
        // Metemos la cuadrícula dentro de un panel extra (margen) para que los botones
        // no choquen violentamente contra los bordes físicos de la ventana.
        Panel margen = new Panel(new BorderLayout());
        margen.add(panelCuadricula, BorderLayout.CENTER);
        add(margen, BorderLayout.CENTER);
    }
}