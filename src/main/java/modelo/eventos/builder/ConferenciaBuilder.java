package modelo.eventos.builder;

import modelo.eventos.Conferencia;
import modelo.eventos.Evento;
import java.time.LocalDateTime;

public class ConferenciaBuilder implements EventoBuilder {

    private String codigo;
    private String nombre;
    private LocalDateTime fecha;
    private String lugar;
    private double precio;
    private int aforo;
    private String url;

    private String ponente = "Ponente";
    private String tema = "Tema";
    private int duracion = 60;

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

    public ConferenciaBuilder conPonente(String ponente) { this.ponente = ponente; return this; }
    public ConferenciaBuilder conTema(String tema) { this.tema = tema; return this; }
    public ConferenciaBuilder conDuracion(int duracion) { this.duracion = duracion; return this; }

    @Override
    public Evento build() {
        return new Conferencia(codigo, nombre, fecha, lugar, aforo, precio, url, ponente, tema, duracion);
    }
}

