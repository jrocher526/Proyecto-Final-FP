package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;
import modelo.Administrador;
import modelo.Camarero;

/**
 * Clase para la gestión de usuarios (Administradores y Camareros).
 * Permite validar accesos y gestionar altas/bajas de personal en MariaDB.
 */
public class UsuarioDAO {
    /**
     * Comprueba si el usuario y contraseña son correctos para dar acceso al panel.
     * @param user Nombre de usuario introducido.
     * @param pass Contraseña introducida.
     * @return Devuelve un objeto Usuario si el login es correcto, o null si falla.
     */
    public Usuario validarLogin(String user, String pass) {
        // Buscamos si existe una coincidencia exacta de credenciales
        String sql = "SELECT * FROM usuarios WHERE nombre = ? AND password = ?";
        Usuario usuarioValidado = null;

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();

            // Si el ResultSet tiene al menos un resultado, el login es correcto
            if (rs.next()) {
                String rol = rs.getString("rol");
                // Dependiendo del rol en la base de datos, creamos un objeto u otro por polimorfismo
                if ("ADMIN".equals(rol)) {
                    usuarioValidado = new Administrador(rs.getInt("id"), rs.getString("nombre"), rs.getString("password"));
                } else {
                    usuarioValidado = new Camarero(rs.getInt("id"), rs.getString("nombre"), rs.getString("password"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return usuarioValidado;
    }

    /**
     * Da de alta a un nuevo camarero en el sistema.
     * @param nombre El nombre del camarero a contratar.
     */
    public void insertarCamarero(String nombre) {
        // Por defecto le ponemos la contraseña 1234 y rol CAMARERO
        String sql = "INSERT INTO usuarios (nombre, password, rol) VALUES (?, '1234', 'CAMARERO')";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Eliminaa un camarero de la base de datos.
     * @param nombre Nombre exacto del empleado a borrar.
     */
    public void eliminarCamarero(String nombre) {
        // Solo borramos si el rol es CAMARERO para proteger la cuenta del ADMIN
        String sql = "DELETE FROM usuarios WHERE nombre = ? AND rol = 'CAMARERO'";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Obtiene una lista solo con los nombres de todos los camareros para pintarlos en los botones de selección.
     * @return Lista de Strings con los nombres.
     */
    public List<String> obtenerNombresCamareros() {
        List<String> camareros = new ArrayList<>();
        // Filtramos para que el Administrador no salga como camarero para atender mesas
        String sql = "SELECT nombre FROM usuarios WHERE rol = 'CAMARERO'";

        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                camareros.add(rs.getString("nombre"));
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return camareros;
    }
}