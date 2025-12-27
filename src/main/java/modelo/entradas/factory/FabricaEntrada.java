package modelo.entradas.factory;

import modelo.entradas.Entrada;
import modelo.eventos.Evento;

public interface FabricaEntrada {
    Entrada crearEntrada(Evento evento);
}
