package controll;

import modelo.eventos.Evento;
import modelo.usuarios.Cliente;

import java.time.LocalDateTime;

public class Venta {

    private final LocalDateTime fecha;
    private final Cliente cliente;
    private final Evento evento;
    private final int cantidad;
    private final String metodoPago;

    public Venta(LocalDateTime fecha, Cliente cliente, Evento evento, int cantidad, String metodoPago) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.evento = evento;
        this.cantidad = cantidad;
        this.metodoPago = metodoPago;
    }

    public LocalDateTime getFecha() { return fecha; }
    public Cliente getCliente() { return cliente; }
    public Evento getEvento() { return evento; }
    public int getCantidad() { return cantidad; }
    public String getMetodoPago() { return metodoPago; }
}
