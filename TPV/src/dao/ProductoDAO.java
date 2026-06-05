package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Categoria;
import modelo.Producto;

/**
 * Clase DAO (Data Access Object) para gestionar los Productos en la base de datos relacional (MariaDB).
 * Se encarga de hacer el CRUD completo (Crear, Leer, Actualizar, Borrar) de la carta del restaurante.
 */
public class ProductoDAO {
    /**
     * Inserta un nuevo producto en la base de datos.
     * @param p El objeto Producto que queremos guardar.
     */
    public void insertarProducto(Producto p) {
        // Consulta SQL preparada con interrogaciones para evitar ataques de inyección SQL
        String sql = "INSERT INTO productos (nombre, categoria, precio) VALUES (?, ?, ?)";

        // Usamos try-with-resources para que la conexión se cierre sola pase lo que pase
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Sustituimos las interrogaciones por los valores reales sacados del objeto
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getCategoria().name()); // Pasamos el Enum a String para guardarlo
            pstmt.setFloat(3, p.getPrecio());

            // Ejecutamos la consulta contra MariaDB
            pstmt.executeUpdate();

        } catch (SQLException e) {
            // Si hay un fallo de conexión o de sintaxis se imprime en consola
            e.printStackTrace();
        }
    }

    /**
     * Modifica el precio de un producto que ya existe.
     * @param nombreViejo El nombre actual del producto que nos sirve para buscarlo (WHERE).
     * @param nuevoPrecio El nuevo precio actualizado.
     */
    public void modificarProducto(String nombreViejo, float nuevoPrecio) {
        String sql = "UPDATE productos SET precio = ? WHERE nombre = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, nuevoPrecio);
            pstmt.setString(2, nombreViejo);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Cambia el nombre de un producto existente por uno nuevo.
     * @param nombreViejo El nombre original para localizar la fila.
     * @param nombreNuevo El nombre definitivo que queremos ponerle.
     */
    public void modificarNombreProducto(String nombreViejo, String nombreNuevo) {
        String sql = "UPDATE productos SET nombre = ? WHERE nombre = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreNuevo);
            pstmt.setString(2, nombreViejo);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Elimina un producto de la carta definitivamente.
     * @param nombre El nombre exacto del producto a borrar.
     */
    public void eliminarProducto(String nombre) {
        // Cuidado con este método, ejecuta un borrado físico en la tabla
        String sql = "DELETE FROM productos WHERE nombre = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Recupera toda la carta de productos de la base de datos.
     * @return Una lista de tipo Producto con todos los registros encontrados.
     */
    public List<Producto> obtenerTodos() {
        // Inizializar  una lista vacía para ir guardando lo que devuelva el SELECT
        List<Producto> lista = new ArrayList<>();
        // Ordenar por categoria y alfabeticamente
        String sql = "SELECT * FROM productos ORDER BY categoria, nombre";

        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // bucle while que va fila por fila de la tabla
            while (rs.next()) {
                // Instanciamos el producto con los datos de la fila actual y lo añadimos a la lista
                lista.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        Categoria.valueOf(rs.getString("categoria")), // Convertimos el String de la BD otra vez a Enum
                        rs.getFloat("precio")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }

        // Devolvemos la lista
        return lista;
    }
}