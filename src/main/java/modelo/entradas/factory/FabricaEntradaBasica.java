package modelo.entradas.factory;

import modelo.entradas.Entrada;
import modelo.entradas.EntradaBasica;
import modelo.eventos.Evento;

public class FabricaEntradaBasica implements FabricaEntrada {
    @Override
    public Entrada crearEntrada(Evento evento) {
        return new EntradaBasica(evento);
    }
}
