package control.command;

import controll.CatalogoEventos;
import modelo.eventos.Evento;

public class ModificarEventoCommand implements Comando {

    private final CatalogoEventos catalogo;
    private final Evento original;
    private final Evento modificado;

    public ModificarEventoCommand(CatalogoEventos catalogo, Evento original, Evento modificado) {
        this.catalogo = catalogo;
        this.original = original;
        this.modificado = modificado;
    }

    @Override
    public void ejecutar() {
        catalogo.modificarEvento(original, modificado);
    }
}
