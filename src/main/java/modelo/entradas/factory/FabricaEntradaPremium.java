package modelo.entradas.factory;

import modelo.entradas.Entrada;
import modelo.entradas.EntradaBasica;
import modelo.entradas.EntradaConConsumicion;
import modelo.eventos.Evento;

public class FabricaEntradaPremium implements FabricaEntrada {

    @Override
    public Entrada crearEntrada(Evento evento) {
        return new EntradaConConsumicion(new EntradaBasica(evento));
    }
}
