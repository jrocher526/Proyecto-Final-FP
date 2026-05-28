package utilidades;

import modelo.Ticket;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class GestorFicheros {

    private static final String RUTA = "data/historialVentas.txt";

    public static void guardarVenta(Ticket ticket) {

        try {

            // Crear carpeta "data" si no existe
            File carpeta = new File("data");

            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            // Abrir archivo en modo añadir contenido
            BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA, true));

            writer.write("--TICKET--");
            writer.newLine();

            writer.write("Fecha: " + new Date());
            writer.newLine();

            writer.write("Ticket Nº: " + ticket.getNumeroTicket());
            writer.newLine();

            writer.write("Mesa: " + ticket.getMesa().getNumero());
            writer.newLine();

            writer.write("Productos:");
            writer.newLine();

            // Recorrer productos del ticket
            for (int i = 0; i < ticket.getProductos().size(); i++) {
                writer.write("- " +
                        ticket.getProductos().get(i).getNombre()
                        + " -> "
                        + ticket.getProductos().get(i).getPrecio()
                        + " €");
                writer.newLine();
            }

            writer.write("TOTAL: " + ticket.getTotal() + " €");
            writer.newLine();

            writer.write("----------------------");
            writer.newLine();
            writer.newLine();

            writer.close();

            System.out.println("Venta guardada correctamente.");

        } catch (IOException e) {

            System.out.println("Error al guardar la venta.");
        }
    }
}