package controll;

import control.command.EliminarEventoCommand;
import control.observer.SujetoEventos;
import excepciones.EventoYaExisteException;
import modelo.eventos.Evento;
import modelo.usuarios.Cliente;
import modelo.ventas.Venta;

import java.util.*;
import javax.swing.JOptionPane;

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
    // 🔥 Notificación persistente 
    String mensaje = "Se ha creado un nuevo evento: " + e.getNombre(); 
    GestorNotificaciones.agregar(mensaje); // 🔥 Notificar a observadores (admin y clientes) e.notificarMensaje(mensaje, e);    
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
    // MÉTODO NUEVO: modificar evento
    public void modificarEvento(Evento original, Evento modificado) {

        if (original == null || modificado == null) {
            throw new IllegalArgumentException("Los eventos no pueden ser nulos.");
        }

        // Reemplazar el evento en el mapa
        eventos.put(original.getCodigo(), modificado);

        // Notificación global persistente
        String mensaje = "El evento '" + original.getNombre() + "' ha sido modificado.";
        GestorNotificaciones.agregar(mensaje);

        // Notificar a los observadores del evento original
        original.notificarMensaje(mensaje, original);
    }

    // MÉTODO MODIFICADO: eliminar evento
    public void eliminarEvento(String codigo) {
        Evento evento = eventos.remove(codigo);

        if (evento != null) {
            String mensaje = "El evento '" + evento.getNombre() + "' ha sido cancelado.";

            GestorNotificaciones.agregar(mensaje);

            evento.notificarMensaje(mensaje, evento);
        }
    }


    



}


