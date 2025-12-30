package modelo.eventos.builder;

import modelo.eventos.Evento;
import modelo.eventos.Teatro;
import java.time.LocalDateTime;

public class TeatroBuilder implements EventoBuilder {

    private String codigo;
    private String nombre;
    private LocalDateTime fecha;
    private String lugar;
    private double precio;
    private int aforo;
    private String url;

    private String compania = "Compañía";
    private int duracion = 120;

    @Override
    public EventoBuilder conCodigo(String codigo) { this.codigo = codigo; return this; }

    @Override
    public EventoBuilder conNombre(String nombre) { this.nombre = nombre; return this; }

    @Override
    public EventoBuilder conFecha(LocalDateTime fecha) { this.fecha = fecha; return this; }

    @Override
    public EventoBuilder conLugar(String lugar) { this.lugar = lugar; return this; }

    @Override
    public EventoBuilder conPrecio(double precio) { this.precio = precio; return this; }

    @Override
    public EventoBuilder conAforo(int aforo) { this.aforo = aforo; return this; }

    @Override
    public EventoBuilder conUrl(String url) { this.url = url; return this; }

    public TeatroBuilder conCompania(String compania) { this.compania = compania; return this; }
    public TeatroBuilder conDuracion(int duracion) { this.duracion = duracion; return this; }

    @Override
    public Evento build() {
        return new Teatro(codigo, nombre, fecha, lugar, aforo, precio, url, compania, duracion);
    }
}
