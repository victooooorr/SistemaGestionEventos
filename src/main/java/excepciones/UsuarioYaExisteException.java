package excepciones;

public class UsuarioYaExisteException extends RuntimeException {
    public UsuarioYaExisteException(String correo) {
        super("El correo '" + correo + "' ya está registrado.");
    }
}
