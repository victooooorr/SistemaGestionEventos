package modelo.eventos.builder;

import modelo.eventos.Evento;
import java.time.LocalDateTime;

public interface EventoBuilder {

    EventoBuilder conCodigo(String codigo);
    EventoBuilder conNombre(String nombre);
    EventoBuilder conFecha(LocalDateTime fecha);
    EventoBuilder conLugar(String lugar);
    EventoBuilder conPrecio(double precio);
    EventoBuilder conAforo(int aforo);
    EventoBuilder conUrl(String url);

    Evento build();
}

