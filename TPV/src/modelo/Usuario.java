package modelo;

public class Usuario {

    protected int id;
    protected String nombre;
    protected String password;

    public Usuario(int id, String nombre, String password) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Usuario: " + nombre;
    }
}