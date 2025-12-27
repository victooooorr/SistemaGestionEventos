package modelo.entradas.factory;

import modelo.entradas.Entrada;
import modelo.entradas.EntradaBasica;
import modelo.entradas.EntradaVIP;
import modelo.eventos.Evento;

public class FabricaEntradaVIP implements FabricaEntrada {
    @Override
    public Entrada crearEntrada(Evento evento) {
        return new EntradaVIP(new EntradaBasica(evento));
    }
}
