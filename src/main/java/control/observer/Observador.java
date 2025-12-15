package control.observer;

import modelo.eventos.Evento;

public interface Observador {
    void actualizar(Evento e);
}
