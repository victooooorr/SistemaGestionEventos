package excepciones;

public class CredencialesIncorrectasException extends RuntimeException {
    public CredencialesIncorrectasException() {
        super("Correo o clave incorrectos.");
    }
}
