package modelo.eventos;

import java.time.LocalDateTime;

public class Teatro extends Evento {
    private String compania;
    private int duracionMin;

    public Teatro(String codigo, String nombre, LocalDateTime fechaHora, String lugar,
                  int aforoMaximo, double precioBase, String compania, int duracionMin) {
        super(codigo, nombre, "Teatro", fechaHora, lugar, aforoMaximo, precioBase);
        this.compania = compania;
        this.duracionMin = duracionMin;
    }

    @Override
    public String mostrarInfo() {
        return String.format("[%s] %s - %s (%d min) %s | Precio: %.2f€ | Aforo disp: %d",
                tipo, nombre, compania, duracionMin, lugar, precioBase, aforoDisponible);
    }
}
