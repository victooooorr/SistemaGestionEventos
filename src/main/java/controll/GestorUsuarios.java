package controll;

import excepciones.UsuarioYaExisteException;
import excepciones.UsuarioNoEncontradoException;
import excepciones.CredencialesIncorrectasException;
import modelo.usuarios.Usuario;

import java.security.MessageDigest;
import java.util.*;

public class GestorUsuarios {
    private final Map<String, Usuario> usuariosPorCorreo = new HashMap<>();
    private final Map<String, String> clavesHash = new HashMap<>();
    private static GestorUsuarios instancia;
    
    public static GestorUsuarios getInstancia() {
        if (instancia == null) {
            instancia = new GestorUsuarios();
        }
    return instancia;
}


    public void altaUsuario(Usuario u, String clave) {
        if (usuariosPorCorreo.containsKey(u.getCorreo())) {
            throw new UsuarioYaExisteException(u.getCorreo());
        }
        usuariosPorCorreo.put(u.getCorreo(), u);
        clavesHash.put(u.getCorreo(), hashClave(clave));
    }

    public Usuario login(String correo, String clave) {
        if (!usuariosPorCorreo.containsKey(correo)) {
            throw new UsuarioNoEncontradoException(correo);
        }
        String hash = hashClave(clave);
        if (!hash.equals(clavesHash.get(correo))) {
            throw new CredencialesIncorrectasException();
        }
        return usuariosPorCorreo.get(correo);
    }

    private String hashClave(String clave) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(clave.getBytes());
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar hash de clave", e);
        }
    }
}


