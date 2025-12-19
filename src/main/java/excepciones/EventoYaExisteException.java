package excepciones;

public class EventoYaExisteException extends RuntimeException {
    public EventoYaExisteException(String codigo) {
        super("El evento con código '" + codigo + "' ya existe en el sistema.");
    }
}
