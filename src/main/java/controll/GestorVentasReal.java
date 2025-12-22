package controll;

import java.util.List;

public class GestorVentasReal implements IVentas {

    public List<Venta> obtenerVentas() {
        return GestorVentas.getInstancia().getVentas();
    }
}
