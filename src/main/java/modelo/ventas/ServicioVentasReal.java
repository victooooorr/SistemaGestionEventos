package modelo.ventas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServicioVentasReal implements ServicioVentas {
    private final List<Venta> ventas = new ArrayList<>();

    @Override
    public List<Venta> consultarVentas() {
        return Collections.unmodifiableList(ventas);
    }

    @Override
    public void registrarVenta(Venta v) {
        ventas.add(v);
    }
}
