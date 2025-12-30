package modelo.eventos;

import java.time.LocalDateTime;

public class Concierto extends Evento {

    private final String genero;
    private final String artista;
    private final int duracion;

    public Concierto(String codigo, String nombre, LocalDateTime fechaHora, String lugar,
                     int aforoMaximo, double precioBase, String urlInfo,
                     String genero, String artista, int duracion) {

        super(codigo, nombre, "Concierto", fechaHora, lugar, aforoMaximo, precioBase, urlInfo);

        this.genero = genero;
        this.artista = artista;
        this.duracion = duracion;
    }
@Override
public Evento clonarConNuevosDatos(String nombre, LocalDateTime fecha, String lugar, double precio, int aforo) {
    return new Concierto(
            getCodigo(),
            nombre,
            fecha,
            lugar,
            aforo,
            precio,
            getUrlInfo(),   // este sí existe en Evento
            this.genero,
            this.artista,
            this.duracion
    );
}


    @Override
    public void mostrarInformacion() {
        super.mostrarInformacion();
        System.out.println("Género: " + genero);
        System.out.println("Artista: " + artista);
        System.out.println("Duración: " + duracion + " min");
    }
    @Override
public String toString() {
    return "[Concierto] " + getNombre() + " - " + getFechaHora() + " - " + getLugar();
}

}
