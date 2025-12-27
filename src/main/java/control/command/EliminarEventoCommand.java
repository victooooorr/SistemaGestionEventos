package control.command;

import controll.CatalogoEventos;

public class EliminarEventoCommand implements Comando {

    private final CatalogoEventos catalogo;
    private final String codigo;

    public EliminarEventoCommand(CatalogoEventos catalogo, String codigo) {
        this.catalogo = catalogo;
        this.codigo = codigo;
    }

    @Override
    public void ejecutar() {
        catalogo.eliminarEvento(codigo);
    }
}

