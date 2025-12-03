package modelo.ventas;

import java.time.LocalDateTime;
import modelo.eventos.Evento;
import modelo.usuarios.Cliente;

public class Venta {
    private final String id;
    private final Cliente cliente;
    private final Evento evento;
    private final int cantidad;
    private final double precioFinal;
    private final LocalDateTime fecha;

    public Venta(String id, Cliente cliente, Evento evento, int cantidad, double precioFinal) {
        this.id = id;
        this.cliente = cliente;
        this.evento = evento;
        this.cantidad = cantidad;
        this.precioFinal = precioFinal;
        this.fecha = LocalDateTime.now();
    }

    public String getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Evento getEvento() { return evento; }
    public int getCantidad() { return cantidad; }
    public double getPrecioFinal() { return precioFinal; }
    public LocalDateTime getFecha() { return fecha; }
}
