public abstract class Usuario {
    protected int id;
    protected String nombre;
    protected String password;

    public boolean login(String pass) {
        return this.password.equals(pass);
    }
}
