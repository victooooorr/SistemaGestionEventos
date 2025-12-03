package modelo.eventos;

import java.time.LocalDateTime;

public class ConciertoFactory implements EventoFactory {
    @Override
    public Evento crearEvento(Object... args) {
        return new Concierto(
            (String) args[0], (String) args[1], (LocalDateTime) args[2], (String) args[3],
            (Integer) args[4], (Double) args[5], (String) args[6], (String) args[7]
        );
    }
}

