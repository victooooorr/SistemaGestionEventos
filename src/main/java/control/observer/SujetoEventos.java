package control.observer;

import java.util.ArrayList;
import java.util.List;

public class SujetoEventos {
    protected final List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador o) {
        observadores.add(o);
    }

    public void eliminarObservador(Observador o) {
        observadores.remove(o);
    }

    public List<Observador> getObservadores() {
    return new ArrayList<>(observadores); // ← COPIA SEGURA
}

}
