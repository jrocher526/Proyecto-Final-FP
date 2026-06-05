package main;

import ui.VentanaLogin;

/**
 * Clase principal de la aplicación.
 * Funciona como el motor de arranque del proyecto, conteniendo el método main
 * que la Máquina Virtual de Java busca para iniciar la ejecución.
 */
public class Main {

    /**
     * Método punto de entrada (entry point) del programa.
     * @param args Argumentos que se podrían pasar por consola al ejecutar (no se usan en este proyecto).
     */
    public static void main(String[] args) {

        // 1. Instanciamos en memoria el primer objeto visual de nuestro TPV: la pantalla de bienvenida.
        // Al hacer el 'new', se ejecuta el constructor de VentanaLogin y se prepara toda su interfaz.
        VentanaLogin ventana = new VentanaLogin();

        // 2. Le pedimos al sistema operativo que dibuje la ventana en la pantalla.
        // Si no ponemos esto en true, el programa estaría corriendo en segundo plano pero sería invisible.
        ventana.setVisible(true);
    }
}