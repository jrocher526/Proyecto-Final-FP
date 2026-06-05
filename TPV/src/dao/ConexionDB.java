package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de utilidad para gestionar la conexión JDBC con la base de datos relacional (MariaDB).
 * Centraliza las credenciales y la URL para que si cambiamos de servidor, solo modifiquemos este archivo.
 */
public class ConexionDB {

    private static final String URL = "jdbc:mariadb://localhost:3306/tpv_restaurante";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Establece y devuelve una conexión activa con el servidor MariaDB.
     * @return Un objeto Connection listo para ejecutar sentencias preparadas (PreparedStatement).
     * @throws SQLException Si el servidor de base de datos está apagado o las credenciales fallan.
     */
    public static Connection getConnection() throws SQLException {
        // El DriverManager busca el driver de MariaDB en las librerías del proyecto y abre el "tubo" de datos
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}