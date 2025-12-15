package control.command;

import controll.GestorEntradas;
import modelo.entradas.Entrada;
import modelo.eventos.Evento;
import modelo.pagos.ContextoPago;
import modelo.usuarios.Cliente;
import modelo.ventas.Venta;

public class ReservarEntrada implements Comando {
    private final GestorEntradas gestor;
    private final Evento evento;
    private final Cliente cliente;
    private final int cantidad;
    private final Entrada entrada;
    private final ContextoPago pago;
    private Venta venta;

    public ReservarEntrada(GestorEntradas gestor, Evento evento, Cliente cliente, int cantidad,
                           Entrada entrada, ContextoPago pago) {
        this.gestor = gestor;
        this.evento = evento;
        this.cliente = cliente;
        this.cantidad = cantidad;
        this.entrada = entrada;
        this.pago = pago;
    }

    @Override
    public void ejecutar() {
        venta = gestor.comprar(evento, cliente, cantidad, entrada, pago);
    }

    public Venta getVenta() { return venta; }
}
