package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import modelo.Ticket;

/**
 * Data Access Object específico para la base de datos orientada a objetos (ObjectDB).
 * Utiliza JPA (Java Persistence API) para guardar el Ticket y todo su contenido sin usar SQL.
 */
public class TicketObjectDBDAO {
    // Ruta donde se va a crear el archivo local de la base de datos de objetos
    private static final String DB_URL = "objectdb/db/tickets.odb";

    /**
     * Guarda un objeto Ticket completo en la base de datos de objetos.
     * @param t El ticket que acaba de ser cobrado.
     */
    public void guardarTicketObjeto(Ticket t) {
        // Levantamos la factoría y el manager para interactuar con ObjectDB
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(DB_URL);
        EntityManager em = emf.createEntityManager();

        try {
            // Iniciamos la transacción (todo o nada)
            em.getTransaction().begin();
            // guarda el ticket, sus productos y los camareros
            em.persist(t);
            // Hacemos commit para confirmar los cambios en disco
            em.getTransaction().commit();
        } catch (Exception e) {
            // Si algo falla, hacemos rollback para no dejar datos a medias
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            // Cerramos conexiones para no saturar la memoria
            em.close();
            emf.close();
        }
    }

    /**
     * Recupera todos los tickets almacenados para poder hacer el cierre de caja.
     * @return Una lista con todos los objetos Ticket de la base de datos.
     */
    public List<Ticket> obtenerTicketsHoy() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(DB_URL);
        EntityManager em = emf.createEntityManager();
        List<Ticket> tickets = new ArrayList<>();

        try {
            // Usando JPOQL pedimos que nos devuelva literalmente objetos de la clase Ticket
            TypedQuery<Ticket> query = em.createQuery("SELECT t FROM Ticket t", Ticket.class);
            tickets = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

        return tickets;
    }
}