package ui;

import java.awt.*;
import java.awt.event.*;

/**
 * Clase de utilidad (Utility Class) que funciona como un motor personalizado de cuadros de diálogo.
 * Sustituye la necesidad de usar JOptionPane (que es de Swing), manteniendo toda
 * la aplicación estrictamente en AWT puro. Todos sus métodos son estáticos para poder
 * llamarlos directamente sin necesidad de instanciar la clase.
 */
public class MensajesAWT {

    /**
     * Muestra una alerta simple de información o error con un botón de "Aceptar".
     * @param parent La ventana padre sobre la que se bloquea este diálogo.
     * @param mensaje El texto que queremos mostrar al usuario.
     * @param titulo El título de la barra superior de la ventanita.
     */
    public static void mostrarMensaje(Window parent, String mensaje, String titulo) {
        // AWT requiere que el 'owner' de un Dialog sea un Frame. Comprobamos si el padre lo es.
        Frame owner = (parent instanceof Frame) ? (Frame) parent : null;

        // Creamos el diálogo. El parámetro 'true' indica que es MODAL (bloquea el resto del programa).
        Dialog d = new Dialog(owner, titulo, true);
        d.setLayout(new BorderLayout(10, 10));

        // Añadimos el texto en el centro
        d.add(new Label(mensaje, Label.CENTER), BorderLayout.CENTER);

        Button btn = new Button("Aceptar");
        // Usamos una función lambda para decirle al botón que cierre y destruya (dispose) la ventana al hacer clic
        btn.addActionListener(e -> d.dispose());

        Panel p = new Panel();
        p.add(btn);
        d.add(p, BorderLayout.SOUTH);

        d.setSize(350, 120);
        d.setLocationRelativeTo(parent);

        d.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { d.dispose(); } });
        d.setVisible(true);
    }

    /**
     * Abre un cuadro de diálogo pidiendo al usuario que escriba un texto.
     * @param valorInicial Texto que aparece escrito por defecto en la caja (puede ser null).
     * @return El texto escrito por el usuario, o null si cancela.
     */
    public static String pedirInput(Window parent, String mensaje, String titulo, String valorInicial) {
        Frame owner = (parent instanceof Frame) ? (Frame) parent : null;
        Dialog d = new Dialog(owner, titulo, true);
        d.setLayout(new GridLayout(3, 1));
        d.add(new Label(mensaje, Label.CENTER));

        TextField txt = new TextField(valorInicial != null ? valorInicial : "");
        Panel pTxt = new Panel(); pTxt.add(txt);
        d.add(pTxt);

        Panel pBotones = new Panel();
        Button btnAceptar = new Button("Aceptar");
        Button btnCancelar = new Button("Cancelar");

        // Usamos un array de 1 posición para poder modificar la variable desde dentro de la función lambda
        String[] result = new String[1];

        btnAceptar.addActionListener(e -> {
            result[0] = txt.getText(); // Guardamos lo que ha escrito
            d.dispose(); // Cerramos
        });
        btnCancelar.addActionListener(e -> d.dispose()); // Si cancela, el array se queda null

        pBotones.add(btnAceptar); pBotones.add(btnCancelar);
        d.add(pBotones);

        d.setSize(350, 150);
        d.setLocationRelativeTo(parent);
        d.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { d.dispose(); } });
        d.setVisible(true); // La ejecución se "pausa" aquí hasta que el usuario cierra el Dialog

        return result[0]; // Devolvemos el contenido del array
    }

    /**
     * Pregunta al usuario si está seguro de realizar una acción (Sí / No).
     * @return true si pulsa 'Sí', false si pulsa 'No' o cierra la ventana.
     */
    public static boolean pedirConfirmacion(Window parent, String mensaje, String titulo) {
        Frame owner = (parent instanceof Frame) ? (Frame) parent : null;
        Dialog d = new Dialog(owner, titulo, true);
        d.setLayout(new BorderLayout(10, 10));
        d.add(new Label(mensaje, Label.CENTER), BorderLayout.CENTER);

        Panel pBotones = new Panel();
        Button btnSi = new Button("Sí");
        Button btnNo = new Button("No");

        // Usamos el mismo patrón del array para extraer el valor booleano desde la lambda
        boolean[] result = new boolean[1];

        btnSi.addActionListener(e -> { result[0] = true; d.dispose(); });
        btnNo.addActionListener(e -> { result[0] = false; d.dispose(); });

        pBotones.add(btnSi); pBotones.add(btnNo);
        d.add(pBotones, BorderLayout.SOUTH);

        d.setSize(400, 120);
        d.setLocationRelativeTo(parent);
        d.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { d.dispose(); } });
        d.setVisible(true);

        return result[0];
    }

    /**
     * Diálogo especial para introducir contraseñas de forma segura (ocultando los caracteres).
     */
    public static String pedirPassword(Window parent, String mensaje) {
        Frame owner = (parent instanceof Frame) ? (Frame) parent : null;
        Dialog d = new Dialog(owner, "Seguridad", true);
        d.setLayout(new GridLayout(3, 1));
        d.add(new Label(mensaje, Label.CENTER));

        TextField txt = new TextField(15);
        // Transforma lo que escribes en asteriscos por seguridad
        txt.setEchoChar('*');

        Panel pTxt = new Panel(); pTxt.add(txt);
        d.add(pTxt);

        Panel pBotones = new Panel();
        Button btnOk = new Button("Entrar");
        Button btnCancelar = new Button("Cancelar");
        String[] result = new String[1];

        btnOk.addActionListener(e -> { result[0] = txt.getText(); d.dispose(); });
        btnCancelar.addActionListener(e -> d.dispose());

        pBotones.add(btnOk); pBotones.add(btnCancelar);
        d.add(pBotones);

        d.setSize(350, 150);
        d.setLocationRelativeTo(parent);
        d.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { d.dispose(); } });
        d.setVisible(true);

        return result[0];
    }

    /**
     * Diálogo que muestra una lista de elementos para que el usuario seleccione uno.
     * @param opciones Array de Strings con las opciones disponibles (ej: nombres de productos).
     * @return El String de la opción seleccionada.
     */
    public static String pedirOpcion(Window parent, String mensaje, String titulo, String[] opciones) {
        Frame owner = (parent instanceof Frame) ? (Frame) parent : null;
        Dialog d = new Dialog(owner, titulo, true);
        d.setLayout(new BorderLayout());
        d.add(new Label(mensaje, Label.CENTER), BorderLayout.NORTH);

        // Usamos java.awt.List para crear un componente visual de lista seleccionable
        java.awt.List lista = new java.awt.List();
        for(String o : opciones) lista.add(o); // Rellenamos la lista visual con los datos del array
        d.add(lista, BorderLayout.CENTER);

        Panel pBotones = new Panel();
        Button btnSel = new Button("Seleccionar");
        Button btnCancel = new Button("Cancelar");
        String[] result = new String[1];

        btnSel.addActionListener(e -> {
            // Solo guardamos el resultado si realmente ha hecho clic en un elemento de la lista
            if(lista.getSelectedItem() != null) {
                result[0] = lista.getSelectedItem();
                d.dispose();
            }
        });
        btnCancel.addActionListener(e -> d.dispose());

        pBotones.add(btnSel); pBotones.add(btnCancel);
        d.add(pBotones, BorderLayout.SOUTH);

        d.setSize(300, 200);
        d.setLocationRelativeTo(parent);
        d.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { d.dispose(); } });
        d.setVisible(true);

        return result[0];
    }
}