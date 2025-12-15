package controll;

import modelo.entradas.Entrada;
import modelo.entradas.EntradaBasica;
import modelo.eventos.Evento;
import modelo.pagos.ContextoPago;
import modelo.usuarios.Cliente;
import modelo.ventas.Ticket;
import modelo.ventas.Venta;

import java.util.UUID;

public class GestorEntradas {

    public Venta comprar(Evento evento, Cliente cliente, int cantidad, Entrada entrada, ContextoPago pago) {
        if (evento == null) throw new IllegalArgumentException("Evento inexistente.");
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad inválida.");
        if (evento.getAforoDisponible() < cantidad) throw new IllegalStateException("Aforo completo.");

        double precioUnitario = (entrada != null ? entrada.getPrecio() : new EntradaBasica(evento).getPrecio());
        double total = precioUnitario * cantidad;

        pago.ejecutarPago(total);

        evento.reducirAforo(cantidad);
        Venta venta = new Venta(UUID.randomUUID().toString(), cliente, evento, cantidad, total);
        CatalogoEventos.getInstancia().registrarVenta(venta);
        Ticket.generar(venta);
        return venta;
    }
}
