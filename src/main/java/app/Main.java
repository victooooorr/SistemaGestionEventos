package app;

import controll.CatalogoEventos;
import controll.GestorEntradas;
import control.command.Invocador;
import control.command.ReservarEntrada;
import modelo.entradas.EntradaBasica;
import modelo.eventos.Concierto;
import modelo.eventos.ConciertoFactory;
import modelo.pagos.ContextoPago;
import modelo.pagos.PagoTarjeta;
import modelo.usuarios.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        var catalogo = CatalogoEventos.getInstancia();

        // Crear un concierto usando la factory
        ConciertoFactory factory = new ConciertoFactory();
        Concierto concierto = (Concierto) factory.crearEvento(
                "EV001", "Rock Night",
                LocalDateTime.now().plusDays(10), "Madrid Arena",
                100, 25.0,
                "https://rocknight.com/info",
                "Rock", "The Flames", 120
        );
        catalogo.agregarEvento(concierto);

        // Crear cliente con fecha de nacimiento
        Cliente cliente = new Cliente(
                "12345678A", "Ana López", "ana@correo.com", "clave", "600000000",
                LocalDate.of(1995, 5, 20)
        );

        // Configurar pago
        ContextoPago pago = new ContextoPago();
        pago.setEstrategia(new PagoTarjeta());

        // Comprar entradas
        GestorEntradas gestor = new GestorEntradas();
        var entrada = new EntradaBasica(concierto);

        var reservar = new ReservarEntrada(gestor, concierto, cliente, 2, entrada, pago);
        var invocador = new Invocador();
        invocador.añadir(reservar);
        invocador.ejecutarTodos();

        System.out.println("Compra realizada. Ticket generado.");
    }
}
