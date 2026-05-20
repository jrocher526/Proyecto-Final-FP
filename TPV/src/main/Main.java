package main;

import enums.Categoria;
import modelo.Camarero;
import modelo.Mesa;
import modelo.Producto;
import modelo.Ticket;
import servicio.SistemaTPV;

public class Main {
    public static void main(String[] args) {
        SistemaTPV sistema = new SistemaTPV();
        Mesa mesa1 = new Mesa(1);
        sistema.añadirMesa(mesa1);

        Camarero camarero = new Camarero(1, "Jhonal", "1234");

        Ticket ticket = camarero.crearTicket(1, mesa1);

        Producto cerveza = new Producto(1, "Cerveza", Categoria.BEBIDA, 2.50);
        Producto hamburguesa = new Producto(2, "Hamburguesa", Categoria.COMIDA, 8.99);

        ticket.añadirProducto(cerveza);
        ticket.añadirProducto(hamburguesa);

        System.out.println(ticket);

        sistema.registrarTicket(ticket);

        sistema.mostrarResumenCaja();
    }
}