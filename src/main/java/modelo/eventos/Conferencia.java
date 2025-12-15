package modelo.eventos;

import java.time.LocalDateTime;

public class Conferencia extends Evento {
    private String ponente;
    private String tematica;
    private int duracionMin;

    public Conferencia(String codigo, String nombre, LocalDateTime fechaHora, String lugar,
                       int aforoMaximo, double precioBase, String urlInfo,
                       String ponente, String tematica, int duracionMin) {
        super(codigo, nombre, "Conferencia", fechaHora, lugar, aforoMaximo, precioBase, urlInfo);
        this.ponente = ponente;
        this.tematica = tematica;
        this.duracionMin = duracionMin;
    }

    @Override
    public String mostrarInfo() {
        return String.format("[%s] %s - %s (%s, %d min) %s | Precio: %.2f€ | Info: %s",
                tipo, nombre, ponente, tematica, duracionMin, lugar, precioBase, urlInfo);
    }
}

