public class Mesa {
    // atributos
    private Integer numero;
    private EstadoMesa estado;

    // constructor
    public Mesa(Integer numero, EstadoMesa estado inicial) {
        this.numero = numero;
        this.estado = estadoInicial;
    }

    /**
     * Cambia el estado actual de la mesa al estado proporcionado.
     * @param e El nuevo estado de la mesa.
     */
    public void cambiarEstado(EstadoMesa e) {
        this.estado = e;
    }

    /**
     * Libera la mesa (puedes definir aquí el estado específico,
     * por ejemplo: EstadoMesa.LIBRE).
     */
    public void liberarMesa() {
        // Implementación lógica para dejar la mesa disponible
        System.out.println("La mesa " + numero + " ya esta libre");
    }

    // Getters
    public Integer getNumero() {
        return numero;
    }

    public EstadoMesa getEstado() {
        return estado;
    }
}