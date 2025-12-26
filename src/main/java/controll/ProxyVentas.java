package controll;

import modelo.usuarios.Administrador;
import modelo.usuarios.Usuario;

import java.util.Collections;
import java.util.List;
import modelo.ventas.Venta;

public class ProxyVentas implements IVentas {

    private final Usuario usuario;
    private final GestorVentasReal real;

    public ProxyVentas(Usuario usuario) {
        this.usuario = usuario;
        this.real = new GestorVentasReal();
    }

    public List<Venta> obtenerVentas() {
        if (usuario instanceof Administrador) {
            return real.obtenerVentas();
        } else {
            System.out.println("Acceso denegado: solo el administrador puede ver las ventas.");
            return Collections.emptyList();
        }
    }
}
