package modelo.ventas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Ticket {
    public static void generar(Venta venta) {
        String nombre = venta.getCliente().getDni() + "_" + venta.getFecha().toString().replace(":", "-") + ".txt";
        String contenido = """
                Ticket de compra
                ----------------
                ID: %s
                Cliente: %s
                Evento: %s
                Cantidad: %d
                Precio final: %.2f€
                Fecha: %s
                """.formatted(
                venta.getId(), venta.getCliente().getNombre(), venta.getEvento().getNombre(),
                venta.getCantidad(), venta.getPrecioFinal(), venta.getFecha().toString());

        try {
            Files.writeString(Path.of(nombre), contenido);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo generar el ticket: " + e.getMessage(), e);
        }
    }
}
