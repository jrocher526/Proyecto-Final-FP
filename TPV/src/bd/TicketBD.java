package bd;

import modelo.Ticket;

import javax.persistence.EntityManager;
import java.util.List;

public class TicketBD {

    public void guardar(Ticket ticket) {

        EntityManager em = ConexionObjectDB.getEntityManager();

        em.getTransaction().begin();

        em.persist(ticket);

        em.getTransaction().commit();

        em.close();
    }

    public List<Ticket> obtenerTodos() {

        EntityManager em = ConexionObjectDB.getEntityManager();

        List<Ticket> tickets =
                em.createQuery("SELECT t FROM Ticket t", Ticket.class).getResultList();

        em.close();
        return tickets;
    }

    public Ticket buscar(int numeroTicket) {

        EntityManager em = ConexionObjectDB.getEntityManager();
        Ticket ticket = em.find(Ticket.class, numeroTicket);
        em.close();

        return ticket;
    }
}