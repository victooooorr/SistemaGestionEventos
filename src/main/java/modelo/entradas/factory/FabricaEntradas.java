package modelo.entradas.factory;

import modelo.entradas.Entrada;
import modelo.eventos.Evento;

public class FabricaEntradas {

    public static Entrada crearEntrada(String tipo, Evento evento) {

        return switch (tipo) {
            case "Básica" -> new FabricaEntradaBasica().crearEntrada(evento);
            case "VIP" -> new FabricaEntradaVIP().crearEntrada(evento);
            case "Premium" -> new FabricaEntradaPremium().crearEntrada(evento);
            default -> throw new IllegalArgumentException("Tipo de entrada no válido: " + tipo);
        };
    }
}

