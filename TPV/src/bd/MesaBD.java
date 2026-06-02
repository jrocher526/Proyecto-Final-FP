package bd;

import modelo.Mesa;

import javax.persistence.EntityManager;
import java.util.List;

public class MesaBD {

    public void guardar(Mesa mesa) {

        EntityManager em = ConexionObjectDB.getEntityManager();

        em.getTransaction().begin();

        em.persist(mesa);

        em.getTransaction().commit();

        em.close();
    }

    public List<Mesa> obtenerTodas() {

        EntityManager em = ConexionObjectDB.getEntityManager();

        List<Mesa> mesas =
                em.createQuery("SELECT m FROM Mesa m", Mesa.class).getResultList();

        em.close();

        return mesas;
    }
}