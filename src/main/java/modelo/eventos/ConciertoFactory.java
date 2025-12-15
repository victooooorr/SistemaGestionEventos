package modelo.eventos;

import java.time.LocalDateTime;

public class ConciertoFactory implements EventoFactory {
    @Override
    public Evento crearEvento(Object... args) {
        return new Concierto(
            (String) args[0],   // código
            (String) args[1],   // nombre
            (LocalDateTime) args[2], // fechaHora
            (String) args[3],   // lugar
            (Integer) args[4],  // aforoMaximo
            (Double) args[5],   // precioBase
            (String) args[6],   // urlInfo
            (String) args[7],   // generoMusical
            (String) args[8],   // artistaPrincipal
            (Integer) args[9]   // duracionMin
        );
    }
}
