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

    // ✅ Método protegido para acceder a la lista desde subclases
    protected List<Observador> getObservadores() {
        return observadores;
    }
}
