package controll;

import java.util.List;
import modelo.ventas.Venta;

public class GestorVentasReal implements IVentas {

    public List<Venta> obtenerVentas() {
        return GestorVentas.getInstancia().getVentas();
    }
}
