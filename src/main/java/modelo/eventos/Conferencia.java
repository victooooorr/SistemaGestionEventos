package modelo.eventos;

import java.time.LocalDateTime;

public class Conferencia extends Evento {

    private final String ponente;
    private final String tema;
    private final int duracion;

    public Conferencia(String codigo, String nombre, LocalDateTime fechaHora, String lugar,
                       int aforoMaximo, double precioBase, String urlInfo,
                       String ponente, String tema, int duracion) {

        super(codigo, nombre, "Conferencia", fechaHora, lugar, aforoMaximo, precioBase, urlInfo);

        this.ponente = ponente;
        this.tema = tema;
        this.duracion = duracion;
    }
@Override
public Evento clonarConNuevosDatos(String nombre, LocalDateTime fecha, String lugar, double precio, int aforo) {
    return new Conferencia(
            getCodigo(),
            nombre,
            fecha,
            lugar,
            aforo,
            precio,
            getUrlInfo(),
            this.ponente,
            this.tema,
            this.duracion
    );
}



    @Override
    public void mostrarInformacion() {
        super.mostrarInformacion();
        System.out.println("Ponente: " + ponente);
        System.out.println("Tema: " + tema);
        System.out.println("Duración: " + duracion + " min");
    }
    @Override
public String toString() {
    return "[Conferencia] " + getNombre() + " - " + getFechaHora() + " - " + getLugar();
}

}

