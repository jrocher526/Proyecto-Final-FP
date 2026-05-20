package modelo;

import enums.EstadoMesa;

/**
 * Esta clase representa las mesas del lugar
 * Sirve para saber que mesa es y si hay alguien sentado o no.
 */
public class Mesa {

    // Atributos
    private int numero;
    private EstadoMesa estado;

    // Constructor
    public Mesa(int numero) {
        this.numero = numero;
        this.estado = EstadoMesa.LIBRE;
    }

    /**
     * Método que permite cambiar el estado de la mesa
     */
    public void cambiarEstado(EstadoMesa estado) {
        this.estado = estado;
    }

    /**
     * Metodo para poner la mesa como LIBRE al momento
     * de que la mesa este limpia o se van los clientes.
     */
    public void liberarMesa() {
        this.estado = EstadoMesa.LIBRE;
    }

    /**
     * Método Para mostrar la información de las mesas
     */
    @Override
    public String toString() {
        return "Mesa " + numero + " - " + estado;
    }

    // Getters y Setters
    public int getNumero() {
    return numero;
    }

    public EstadoMesa getEstado() {
    return estado;
    }
}