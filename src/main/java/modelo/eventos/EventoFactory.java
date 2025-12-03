package modelo.eventos;

public interface EventoFactory {
    Evento crearEvento(Object... args);
}
