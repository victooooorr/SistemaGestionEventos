package controll;

import control.observer.SujetoEventos;
import excepciones.EventoYaExisteException;
import modelo.eventos.Evento;
import modelo.eventos.EstadoEvento; // Importante
import modelo.usuarios.Cliente;
import modelo.ventas.Venta;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

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
        // Notificar creación
        String mensaje = "Se ha creado un nuevo evento: " + e.getNombre();
        GestorNotificaciones.agregar(mensaje); 
        
        getObservadores().stream()
                .filter(o -> o instanceof Cliente)
                .map(o -> (Cliente) o)
                .filter(c -> c.getPreferencias() != null && c.getPreferencias().coincide(e))
                .forEach(c -> c.actualizar(e));
    }

    public Evento buscarEvento(String codigo) {
        return eventos.get(codigo);
    }
    
    // --- NUEVO MÉTODO DE BÚSQUEDA AVANZADA ---
    public List<Evento> buscarEventosAvanzado(String texto, String tipo, LocalDate fechaInicio) {
        return eventos.values().stream()
            .filter(e -> {
                // 1. Filtro de texto (nombre o lugar)
                boolean matchTexto = (texto == null || texto.isEmpty()) ||
                        e.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                        e.getLugar().toLowerCase().contains(texto.toLowerCase());
                
                // 2. Filtro de tipo
                boolean matchTipo = (tipo == null || tipo.equals("Todos")) ||
                        e.getTipo().equalsIgnoreCase(tipo);

                // 3. Filtro de fecha (eventos posteriores a la fecha dada)
                boolean matchFecha = (fechaInicio == null) ||
                        e.getFechaHora().toLocalDate().isAfter(fechaInicio.minusDays(1));

                return matchTexto && matchTipo && matchFecha;
            })
            .collect(Collectors.toList());
    }

    public void registrarVenta(Venta v) {
        ventas.add(v);
    }
    
    public Collection<Evento> listarEventos() {
        return Collections.unmodifiableCollection(eventos.values());
    }

    public void modificarEvento(Evento original, Evento modificado) {
        if (original == null || modificado == null) throw new IllegalArgumentException("Nulos no permitidos");
        eventos.put(original.getCodigo(), modificado);
        String mensaje = "El evento '" + original.getNombre() + "' ha sido modificado.";
        GestorNotificaciones.agregar(mensaje);
        original.notificarMensaje(mensaje, original);
    }

    public void eliminarEvento(String codigo) {
        Evento evento = eventos.remove(codigo);
        if (evento != null) {
            evento.setEstado(EstadoEvento.CANCELADO); // Cambiamos estado antes de borrar ref lógica
            String mensaje = "El evento '" + evento.getNombre() + "' ha sido cancelado.";
            GestorNotificaciones.agregar(mensaje);
            evento.notificarMensaje(mensaje, evento);
        }
    }
}

