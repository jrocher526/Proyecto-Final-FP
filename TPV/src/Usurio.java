public abstract class Usuario {
    protected int id;
    protected String nombre;
    protected String password;

    public boolean login(String pass) {
        return this.password.equals(pass);
    }
}

// Camarero.java
package com.tpv.usuarios;
import com.tpv.modelos.Mesa;

public class Camarero extends Usuario {
    public void seleccionarMesa(Mesa m) {  }
    public void crearTicket(Mesa m) { }
}
