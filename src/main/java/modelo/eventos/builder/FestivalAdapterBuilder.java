package modelo.eventos.builder;

import modelo.eventos.Evento;
import modelo.eventos.Festival;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modelo.eventos.ComponenteEvento;

public class FestivalAdapterBuilder implements EventoBuilder {

    private String codigo;
    private String nombre;
    private LocalDateTime fecha;
    private String lugar;
    private double precio;
    private int aforo;
    private String url;

    @Override
    public EventoBuilder conCodigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    @Override
    public EventoBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    @Override
    public EventoBuilder conFecha(LocalDateTime fecha) {
        this.fecha = fecha;
        return this;
    }

    @Override
    public EventoBuilder conLugar(String lugar) {
        this.lugar = lugar;
        return this;
    }

    @Override
    public EventoBuilder conPrecio(double precio) {
        this.precio = precio;
        return this;
    }

    @Override
    public EventoBuilder conAforo(int aforo) {
        this.aforo = aforo;
        return this;
    }

    @Override
    public EventoBuilder conUrl(String url) {
        this.url = url;
        return this;
    }
    private List<ComponenteEvento> subeventos = new ArrayList<>();

    public FestivalAdapterBuilder conSubevento(ComponenteEvento sub) {
        subeventos.add(sub);
        return this;
    }

    public FestivalAdapterBuilder conSubeventos(List<ComponenteEvento> lista) {
        subeventos.addAll(lista);
        return this;
    }


    @Override
    public Evento build() {
        // Construcción del Festival usando el constructor real
        Festival festival = new Festival(
                codigo,
                nombre,
                fecha,
                lugar,
                aforo,
                precio,
                url
        );

        for (ComponenteEvento c : subeventos) { 
            festival.agregarSubevento(c); }

        return festival;
    }
}
