package modelo.ventas;

import modelo.entradas.Entrada;
import modelo.eventos.Evento;
import modelo.usuarios.Cliente;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Ticket {

    public static File generar(Venta venta) {

        Cliente cliente = venta.getCliente();
        Evento evento = venta.getEvento();

        File carpeta = new File("tickets");
        if (!carpeta.exists()) carpeta.mkdir();

        File archivo = new File(carpeta,
                "ticket_" + cliente.getDni() + "_" + evento.getCodigo() + ".txt");

        try (FileWriter writer = new FileWriter(archivo)) {

            writer.write("===== TICKET =====\n");
            writer.write("Fecha de compra: " + venta.getFecha() + "\n\n");

            writer.write("Cliente:\n");
            writer.write("Nombre: " + cliente.getNombre() + "\n");
            writer.write("Correo: " + cliente.getCorreo() + "\n\n");

            writer.write("Evento:\n");
            writer.write("Código: " + evento.getCodigo() + "\n");
            writer.write("Nombre: " + evento.getNombre() + "\n");
            writer.write("Fecha: " + evento.getFechaHora() + "\n");
            writer.write("Lugar: " + evento.getLugar() + "\n\n");

            writer.write("Cantidad: " + venta.getCantidad() + "\n");
            writer.write("Precio total: " + venta.getPrecioFinal() + " €\n");

            writer.write("==========================\n");

        } catch (IOException e) {
            throw new RuntimeException("Error al generar el ticket");
        }

        return archivo;
    }
}
