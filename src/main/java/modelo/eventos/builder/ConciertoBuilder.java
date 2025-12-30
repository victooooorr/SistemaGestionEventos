package modelo.eventos.builder;

import modelo.eventos.Concierto;
import modelo.eventos.Evento;
import java.time.LocalDateTime;

public class ConciertoBuilder implements EventoBuilder {

    private String codigo;
    private String nombre;
    private LocalDateTime fecha;
    private String lugar;
    private double precio;
    private int aforo;
    private String url;

    private String genero = "Desconocido";
    private String artista = "Artista";
    private int duracion = 90;

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

    // Extras específicos
    public ConciertoBuilder conGenero(String genero) { this.genero = genero; return this; }
    public ConciertoBuilder conArtista(String artista) { this.artista = artista; return this; }
    public ConciertoBuilder conDuracion(int duracion) { this.duracion = duracion; return this; }

    @Override
    public Evento build() {
        return new Concierto(codigo, nombre, fecha, lugar, aforo, precio, url, genero, artista, duracion);
    }
}
