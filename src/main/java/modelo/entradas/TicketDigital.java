package modelo.entradas;

import modelo.eventos.Evento;
import modelo.usuarios.Cliente;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class TicketDigital {

    public static File generarTicket(Cliente cliente, Evento evento, int cantidad, String metodoPago, Entrada entrada) {

        File carpeta = new File("tickets");
        if (!carpeta.exists()) carpeta.mkdir();

        File archivo = new File(carpeta, "ticket_" + cliente.getDni() + "_" + evento.getCodigo() + ".txt");

        try (FileWriter writer = new FileWriter(archivo)) {

            writer.write("===== TICKET DIGITAL =====\n");
            writer.write("Fecha de compra: " + LocalDateTime.now() + "\n\n");

            writer.write("Cliente:\n");
            writer.write("Nombre: " + cliente.getNombre() + "\n");
            writer.write("Correo: " + cliente.getCorreo() + "\n\n");

            writer.write("Evento:\n");
            writer.write("Código: " + evento.getCodigo() + "\n");
            writer.write("Nombre: " + evento.getNombre() + "\n");
            writer.write("Fecha: " + evento.getFechaHora() + "\n");
            writer.write("Lugar: " + evento.getLugar() + "\n\n");

            writer.write("Tipo de entrada: " + entrada.getDescripcion() + "\n");
            writer.write("Precio unitario: " + entrada.getPrecio() + " €\n");
            writer.write("Cantidad: " + cantidad + "\n");
            writer.write("Precio total: " + (entrada.getPrecio() * cantidad) + " €\n");
            writer.write("Método de pago: " + metodoPago + "\n");

            writer.write("==========================\n");

        } catch (IOException e) {
            throw new RuntimeException("Error al generar el ticket digital");
        }

        return archivo;
    }
}



