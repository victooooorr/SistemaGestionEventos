package excepciones;

public class EventoNoEncontradoException extends RuntimeException {
    public EventoNoEncontradoException(String codigo) {
        super("No se encontró el evento con código '" + codigo + "'.");
    }
}
