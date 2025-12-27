package control.observer;

import java.util.ArrayList;
import java.util.List;
import modelo.eventos.Evento;

public class SujetoEventos {

    private final List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador o) {
        observadores.add(o);
    }

    public void eliminarObservador(Observador o) {
        observadores.remove(o);
    }

    // 🔥 CORREGIDO: devolvemos una COPIA para evitar ConcurrentModificationException
    public List<Observador> getObservadores() {
        return new ArrayList<>(observadores);
    }

    // 🔔 Notificación estándar
    public void notificar(Evento evento) {
        for (Observador o : getObservadores()) {
            o.actualizar(evento);
        }
    }

    // 🔔 Notificación con mensaje personalizado
    public void notificarMensaje(String mensaje, Evento evento) {
        for (Observador o : getObservadores()) {
            o.actualizarMensaje(mensaje, evento);
        }
    }
}

