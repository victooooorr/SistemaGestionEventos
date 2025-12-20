package controll;

import modelo.eventos.Evento;
import modelo.usuarios.Cliente;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestorVentas {

    private static GestorVentas instancia;

    private final List<Venta> ventas = new ArrayList<>();

    private GestorVentas() {}

    public static GestorVentas getInstancia() {
        if (instancia == null) instancia = new GestorVentas();
        return instancia;
    }

    public void registrarVenta(Cliente cliente, Evento evento, int cantidad, String metodoPago) {
        ventas.add(new Venta(LocalDateTime.now(), cliente, evento, cantidad, metodoPago));
    }

    public List<Venta> getVentas() {
        return ventas;
    }
}

