package controll;

import control.observer.SujetoEventos;
import modelo.eventos.Evento;
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
            throw new IllegalArgumentException("Código de evento ya existente: " + e.getCodigo());
        }
        eventos.put(e.getCodigo(), e);
        notificar(e);
    }

    public Evento buscarEvento(String codigo) { return eventos.get(codigo); }

    public Collection<Evento> listarEventos() { return Collections.unmodifiableCollection(eventos.values()); }

    public void registrarVenta(Venta v) { ventas.add(v); }

    public List<Venta> listarVentas() { return Collections.unmodifiableList(ventas); }
}
