package modelo.ventas;

import modelo.usuarios.Administrador;
import modelo.usuarios.Usuario;
import java.util.List;

public class ProxyVentas implements ServicioVentas {
    private final ServicioVentas servicioReal;
    private final Usuario usuario;

    public ProxyVentas(ServicioVentas servicioReal, Usuario usuario) {
        this.servicioReal = servicioReal;
        this.usuario = usuario;
    }

    @Override
    public List<Venta> consultarVentas() {
        if (!(usuario instanceof Administrador)) {
            throw new SecurityException("Acceso denegado: solo administradores pueden consultar ventas.");
        }
        return servicioReal.consultarVentas();
    }

    @Override
    public void registrarVenta(Venta v) {
        servicioReal.registrarVenta(v);
    }
}

