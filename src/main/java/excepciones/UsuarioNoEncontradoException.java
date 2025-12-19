package excepciones;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(String correo) {
        super("No existe un usuario con correo '" + correo + "'.");
    }
}
