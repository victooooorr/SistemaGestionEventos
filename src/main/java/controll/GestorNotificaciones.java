package controll;

import java.util.ArrayList;
import java.util.List;

public class GestorNotificaciones {

    private static final List<String> notificaciones = new ArrayList<>();

    public static void agregar(String mensaje) {
        notificaciones.add(mensaje);
    }

    public static List<String> obtener() {
        return new ArrayList<>(notificaciones); // copia segura
    }

    public static void limpiar() {
        notificaciones.clear();
    }
}
