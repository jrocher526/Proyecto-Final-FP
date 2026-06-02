package bd;


import modelo.Producto;

import javax.persistence.EntityManager;
import java.util.List;

public class ProductoBD {

    public void guardar(Producto producto) {

        EntityManager em = ConexionObjectDB.getEntityManager();

        em.getTransaction().begin();

        em.persist(producto);

        em.getTransaction().commit();

        em.close();
    }

    public List<Producto> obtenerTodos() {

        EntityManager em = ConexionObjectDB.getEntityManager();

        List<Producto> productos =
                em.createQuery(
                        "SELECT p FROM Producto p",
                        Producto.class
                ).getResultList();

        em.close();

        return productos;
    }

    public void eliminar(int id) {

        EntityManager em = ConexionObjectDB.getEntityManager();

        em.getTransaction().begin();

        Producto producto = em.find(Producto.class, id);

        if (producto != null) {

            em.remove(producto);
        }

        em.getTransaction().commit();

        em.close();
    }
}