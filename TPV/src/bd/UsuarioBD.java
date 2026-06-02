package bd;

import modelo.Usuario;

import javax.persistence.EntityManager;
import java.util.List;

public class UsuarioBD {

    public void guardar(Usuario usuario) {

        EntityManager em = ConexionObjectDB.getEntityManager();

        em.getTransaction().begin();

        em.persist(usuario);

        em.getTransaction().commit();

        em.close();
    }

    public List<Usuario> obtenerTodos() {

        EntityManager em = ConexionObjectDB.getEntityManager();

        List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();

        em.close();

        return usuarios;
    }

    public Usuario buscar(int id) {

        EntityManager em = ConexionObjectDB.getEntityManager();

        Usuario usuario = em.find(Usuario.class, id);

        em.close();

        return usuario;
    }
}