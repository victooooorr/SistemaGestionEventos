package modelo.eventos;

import java.time.LocalDateTime;

public class Conferencia extends Evento {
    private String ponente;
    private String tematica;

    public Conferencia(String codigo, String nombre, LocalDateTime fechaHora, String lugar,
                       int aforoMaximo, double precioBase, String ponente, String tematica) {
        super(codigo, nombre, "Conferencia", fechaHora, lugar, aforoMaximo, precioBase);
        this.ponente = ponente;
        this.tematica = tematica;
    }

    @Override
    public String mostrarInfo() {
        return String.format("[%s] %s - %s (%s) %s | Precio: %.2f€ | Aforo disp: %d",
                tipo, nombre, ponente, tematica, lugar, precioBase, aforoDisponible);
    }
}
