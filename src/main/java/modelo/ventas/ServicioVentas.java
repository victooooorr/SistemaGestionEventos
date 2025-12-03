package modelo.ventas;

import java.util.List;

public interface ServicioVentas {
    List<Venta> consultarVentas();
    void registrarVenta(Venta v);
}
