package modelo;

import enums.EstadoMesa;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Mesa {

    @Id
    private int numero;

    @Enumerated(EnumType.STRING)
    private EstadoMesa estado;

    public Mesa() {
    }

    public Mesa(int numero) {
        this.numero = numero;
        this.estado = EstadoMesa.LIBRE;
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

    public int getNumero() {
        return numero;
    }

    public EstadoMesa getEstado() {
        return estado;
    }
}