package excepciones;

public class AforoCompletoException extends RuntimeException {
    public AforoCompletoException(String nombre) {
        super("El evento '" + nombre + "' ya no tiene aforo disponible.");
    }
}
