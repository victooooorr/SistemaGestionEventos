package controll;

import control.observer.SujetoEventos;
import excepciones.EventoYaExisteException;
import modelo.eventos.Evento;
import modelo.usuarios.Cliente;
import modelo.ventas.Venta;

import java.util.*;

public class CatalogoEventos extends SujetoEventos {

    private static CatalogoEventos instancia;
    private final Map<String, Evento> eventos = new HashMap<>();
     private final List<Venta> ventas = new ArrayList<>();

    private CatalogoEventos() {}

    public static synchronized CatalogoEventos getInstancia() {
        if (instancia == null) instancia = new CatalogoEventos();
        return instancia;
    }

    public void agregarEvento(Evento e) {
    if (eventos.containsKey(e.getCodigo())) {
        throw new EventoYaExisteException(e.getCodigo());
    }

    eventos.put(e.getCodigo(), e);

    // ✅ Notificar solo a clientes con preferencias que coincidan
    getObservadores().stream()
            .filter(o -> o instanceof Cliente)
            .map(o -> (Cliente) o)
            .filter(c -> c.getPreferencias() != null)
            .filter(c -> c.getPreferencias().coincide(e))
            .forEach(c -> c.actualizar(e));
}


    public Evento buscarEvento(String codigo) {
        return eventos.get(codigo);
    }
     public void registrarVenta(Venta v) {
        ventas.add(v);
    }
    public Collection<Evento> listarEventos() {
        return Collections.unmodifiableCollection(eventos.values());
    }
    public void eliminarEvento(String codigo) {
    eventos.remove(codigo);
}

}


