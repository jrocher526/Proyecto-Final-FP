package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Clase encargada de gestionar la conexión con la base de datos orientada a objetos (ObjectDB).
 * Utiliza el estándar JPA para la persistencia.
 */
public class ConexionObjectDB {

    // Variable estática: La factoría es pesada, así que solo tendremos UNA instancia compartida en toda la app
    private static EntityManagerFactory emf;

    // Bloque estático: Este código se ejecuta de forma automática una sola vez cuando el programa arranca
    static {
        // Genera el archivo local de la base de datos si no existe, o se conecta a él si ya está creado
        emf = Persistence.createEntityManagerFactory("db/tickets_historial.odb");
    }

    /**
     * Genera un gestor de entidades (EntityManager). Es el objeto que los DAOs usan
     * para hacer el equivalente a los INSERTS o SELECTS (persist, find, createQuery...).
     * @return EntityManager listo para abrir transacciones con la base de datos.
     */
    public static EntityManager getEntityManager() {
        // Cada vez que el DAO necesita guardar un Ticket, le pide un manager "ligero" a la factoría central
        return emf.createEntityManager();
    }

    /**
     * Cierra la factoría general para liberar recursos del ordenador.
     * Lo ideal es llamar a este método justo antes de que el usuario cierre el programa por completo.
     */
    public static void cerrar() {
        // Comprobación de seguridad: Solo lo cerramos si existe y si de verdad sigue abierto
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}