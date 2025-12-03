package modelo.eventos;

import java.time.LocalDateTime;

public class Concierto extends Evento {
    private String generoMusical;
    private String artistaPrincipal;

    public Concierto(String codigo, String nombre, LocalDateTime fechaHora, String lugar,
                     int aforoMaximo, double precioBase, String generoMusical, String artistaPrincipal) {
        super(codigo, nombre, "Concierto", fechaHora, lugar, aforoMaximo, precioBase);
        this.generoMusical = generoMusical;
        this.artistaPrincipal = artistaPrincipal;
    }

    @Override
    public String mostrarInfo() {
        return String.format("[%s] %s - %s (%s) %s | Precio: %.2f€ | Aforo disp: %d",
                tipo, nombre, artistaPrincipal, generoMusical, lugar, precioBase, aforoDisponible);
    }
}
