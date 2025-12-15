package control.observer;

import java.util.ArrayList;
import java.util.List;
import modelo.eventos.Evento;

public class SujetoEventos {
    private final List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador o) { observadores.add(o); }
    public void eliminarObservador(Observador o) { observadores.remove(o); }

    protected void notificar(Evento e) {
        observadores.forEach(o -> o.actualizar(e));
    }
}
