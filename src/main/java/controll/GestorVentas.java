package controll;

import modelo.eventos.Evento;
import modelo.usuarios.Cliente;
import modelo.ventas.Venta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GestorVentas {

    private static GestorVentas instancia;

    private final List<Venta> ventas = new ArrayList<>();

    private GestorVentas() {}

    public static GestorVentas getInstancia() {
        if (instancia == null) instancia = new GestorVentas();
        return instancia;
    }

    public void registrarVenta(Cliente cliente, Evento evento, int cantidad, double precioFinal) {

        Venta venta = new Venta(
                UUID.randomUUID().toString(),
                cliente,
                evento,
                cantidad,
                precioFinal
        );

        ventas.add(venta);
    }

    public List<Venta> getVentas() {
        return ventas;
    }
}


