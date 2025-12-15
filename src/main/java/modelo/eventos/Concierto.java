package modelo.eventos;

import java.time.LocalDateTime;

public class Concierto extends Evento {
    private String generoMusical;
    private String artistaPrincipal;
    private int duracionMin;

    public Concierto(String codigo, String nombre, LocalDateTime fechaHora, String lugar,
                     int aforoMaximo, double precioBase, String urlInfo,
                     String generoMusical, String artistaPrincipal, int duracionMin) {
        super(codigo, nombre, "Concierto", fechaHora, lugar, aforoMaximo, precioBase, urlInfo);
        this.generoMusical = generoMusical;
        this.artistaPrincipal = artistaPrincipal;
        this.duracionMin = duracionMin;
    }

    @Override
    public String mostrarInfo() {
        return String.format("[%s] %s - %s (%s, %d min) %s | Precio: %.2f€ | Info: %s",
                tipo, nombre, artistaPrincipal, generoMusical, duracionMin, lugar, precioBase, urlInfo);
    }
}

