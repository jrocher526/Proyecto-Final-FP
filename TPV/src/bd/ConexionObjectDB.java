package bd;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConexionObjectDB {


    private static final String UNIDAD_PERSISTENCIA = "TPV";

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA);

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void cerrarConexion() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}