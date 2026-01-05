package controll;

import excepciones.AforoCompletoException;
import excepciones.EventoNoEncontradoException;
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

        if (evento == null) throw new EventoNoEncontradoException("null");

        if (evento.getAforoDisponible() < cantidad) {
            throw new AforoCompletoException(evento.getNombre());
        }

        double precioUnitario = (entrada != null ? entrada.getPrecio() : new EntradaBasica(evento).getPrecio());
        double total = precioUnitario * cantidad;

        // 1. Ejecutar pago
        pago.ejecutarPago(total);

        // 2. Reducir aforo
        evento.reducirAforo(cantidad);

        // 3. Crear el objeto Venta
        Venta venta = new Venta(UUID.randomUUID().toString(), cliente, evento, cantidad, total);
        
        // 4. Registrar en el Catálogo (Histórico general)
        CatalogoEventos.getInstancia().registrarVenta(venta);
        
        // 🔥 5. REGISTRAR EN GESTOR DE VENTAS (¡IMPORTANTE PARA EL ADMIN!)
        GestorVentas.getInstancia().agregarVenta(venta);
        
        // 6. Generar el archivo físico del ticket
        Ticket.generar(venta);

        return venta;
    }
}
