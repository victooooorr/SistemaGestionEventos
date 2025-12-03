package modelo.eventos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Festival extends Evento implements ComponenteEvento {
    private final List<ComponenteEvento> subeventos = new ArrayList<>();

    public Festival(String codigo, String nombre, LocalDateTime fechaHora, String lugar,
                    int aforoMaximo, double precioBase) {
        super(codigo, nombre, "Festival", fechaHora, lugar, aforoMaximo, precioBase);
    }

    public void agregarEvento(ComponenteEvento e) { subeventos.add(e); }
    public void eliminarEvento(ComponenteEvento e) { subeventos.remove(e); }
    public List<ComponenteEvento> getSubeventos() { return List.copyOf(subeventos); }

    @Override
    public String mostrarInfo() {
        return String.format("[%s] %s (%d actividades) %s | Precio base: %.2f€ | Aforo disp: %d",
                tipo, nombre, subeventos.size(), lugar, precioBase, aforoDisponible);
    }

    @Override
    public void mostrarInfoCompuesto() {
        System.out.println(mostrarInfo());
        subeventos.forEach(ComponenteEvento::mostrarInfoCompuesto);
    }
}
