package modelo;

import enums.EstadoMesa;

public class Mesa {
    private int numero;
    private EstadoMesa estado;

    public Mesa(int numero) {
        this.numero = numero;
        this.estado = EstadoMesa.LIBRE;
    }

    public int getNumero() {
        return numero;
    }

    public EstadoMesa getEstado() {
        return estado;
    }

    public void cambiarEstado(EstadoMesa estado) {
        this.estado = estado;
    }

    public void liberarMesa() {
        this.estado = EstadoMesa.LIBRE;
    }

    @Override
    public String toString() {
        return "Mesa " + numero + " - " + estado;
    }
}