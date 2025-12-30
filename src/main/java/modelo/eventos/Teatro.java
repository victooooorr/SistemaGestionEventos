package modelo.eventos;

import java.time.LocalDateTime;

public class Teatro extends Evento {
    private String compania;
    private int duracionMin;

    public Teatro(String codigo, String nombre, LocalDateTime fechaHora, String lugar,
                  int aforoMaximo, double precioBase, String urlInfo,
                  String compania, int duracionMin) {
        super(codigo, nombre, "Teatro", fechaHora, lugar, aforoMaximo, precioBase, urlInfo);
        this.compania = compania;
        this.duracionMin = duracionMin;
    }

    public String mostrarInfo() {
        return String.format("[%s] %s - %s (%d min) %s | Precio: %.2f€ | Info: %s | Aforo disp: %d",
                tipo, nombre, compania, duracionMin, lugar, precioBase, urlInfo, aforoDisponible);
    }
    @Override
public Evento clonarConNuevosDatos(String nombre, LocalDateTime fecha, String lugar, double precio, int aforo) {
    return new Teatro(
            getCodigo(),
            nombre,
            fecha,
            lugar,
            aforo,
            precio,
            getUrlInfo(),
            this.compania,
            this.duracionMin
    );
}
@Override
public String toString() {
    return "[Teatro] " + getNombre() + " - " + getFechaHora() + " - " + getLugar();
}

}
