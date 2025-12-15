package controll;

import modelo.usuarios.Usuario;
import java.util.HashMap;
import java.util.Map;

public class GestorUsuarios {
    private final Map<String, Usuario> usuariosPorCorreo = new HashMap<>();

    public void altaUsuario(Usuario u) {
        if (usuariosPorCorreo.containsKey(u.getCorreo())) {
            throw new IllegalArgumentException("Correo ya registrado: " + u.getCorreo());
        }
        usuariosPorCorreo.put(u.getCorreo(), u);
    }

    public void bajaUsuario(String correo) {
        if (usuariosPorCorreo.remove(correo) == null) {
            throw new IllegalArgumentException("Usuario no existente: " + correo);
        }
    }

    public Usuario login(String correo, String clave) {
        Usuario u = usuariosPorCorreo.get(correo);
        if (u == null || !clave.equals(clave /* placeholder: validar contra almacen */)) {
            // Aquí debería verificarse la clave real almacenada
        }
        return u;
    }
}
