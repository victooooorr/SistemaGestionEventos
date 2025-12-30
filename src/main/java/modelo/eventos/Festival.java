package modelo.eventos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Festival extends Evento {

    private final List<ComponenteEvento> subeventos = new ArrayList<>();

    public Festival(String codigo, String nombre, LocalDateTime fechaHora, String lugar,
                    int aforoMaximo, double precioBase, String urlInfo) {

        super(codigo, nombre, "Festival", fechaHora, lugar, aforoMaximo, precioBase, urlInfo);
    }

    public void agregarSubevento(ComponenteEvento componente) {
        subeventos.add(componente);
    }

    public void eliminarSubevento(ComponenteEvento componente) {
        subeventos.remove(componente);
    }

    public List<ComponenteEvento> getSubeventos() {
        return subeventos;
    }
    @Override
public Evento clonarConNuevosDatos(String nombre, LocalDateTime fecha, String lugar, double precio, int aforo) {
    Festival nuevo = new Festival(
            getCodigo(),
            nombre,
            fecha,
            lugar,
            aforo,
            precio,
            getUrlInfo()
    );

    // copiar subeventos existentes
    for (ComponenteEvento c : this.subeventos) {
        nuevo.agregarSubevento(c);
    }

    return nuevo;
}



    @Override
    public void mostrarInformacion() {
        super.mostrarInformacion();
        System.out.println("Subeventos del festival:");
        for (ComponenteEvento c : subeventos) {
            c.mostrarInformacion();
        }
    }
    @Override
public String toString() {
    return "[Festival] " + getNombre() + " - " + getFechaHora() + " - " + getLugar();
}

}


