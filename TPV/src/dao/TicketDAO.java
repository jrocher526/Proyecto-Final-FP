package dao;

import java.sql.*;
import modelo.Ticket;

/**
 * DAO para la tabla 'tickets' en la base de datos relacional (MariaDB).
 * A diferencia de ObjectDB (que guarda todos los productos y camareros),
 * aquí solo guardamos un resumen básico de la venta (el total cobrado y las observaciones)
 * para tener un registro relacional rápido y ligero.
 */
public class TicketDAO {

    /**
     * Inserta un nuevo registro de una venta en la base de datos MariaDB.
     * @param t El objeto Ticket del que vamos a extraer el dinero total y las notas.
     */
    public void guardarTicket(Ticket t) {
        // Preparamos la consulta SQL con interrogaciones para evitar inyección SQL
        String sql = "INSERT INTO tickets (total, observaciones) VALUES (?, ?)";

        // Usamos try-with-resources para que Java cierre la conexión a la BD automáticamente al terminar,
        // así evitamos que el servidor de MariaDB se sature por dejar conexiones abiertas.
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Sustituimos el primer '?' por el dinero total del ticket (float)
            pstmt.setFloat(1, t.getTotal());

            // Sustituimos el segundo '?' por el texto de observaciones (String)
            pstmt.setString(2, t.getObservaciones());

            // Ejecutamos la inserción para que se cree la nueva fila en la tabla
            pstmt.executeUpdate();

        } catch (SQLException e) {
            // Si la tabla no existe o hay error de conexión, mostramos la traza del error
            // en la consola para saber exactamente dónde ha petado
            e.printStackTrace();
        }
    }
}