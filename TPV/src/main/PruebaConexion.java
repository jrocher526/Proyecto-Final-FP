package main;

import bd.ConexionObjectDB;
import javax.persistence.EntityManager;

public class PruebaConexion {

    public static void main(String[] args) {

        EntityManager em = ConexionObjectDB.getEntityManager();

        if (em != null) {

            System.out.println("Conexión correcta con ObjectDB");
        }

        em.close();

        ConexionObjectDB.cerrarConexion();
    }
}