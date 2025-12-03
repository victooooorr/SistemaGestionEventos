package modelo.entradas;

import modelo.eventos.Evento;

public class EntradaBasica implements Entrada {
    protected Evento evento;

    public EntradaBasica(Evento evento) { this.evento = evento; }

    @Override
    public String getDescripcion() {
        return "Entrada básica para " + evento.getNombre();
    }

    @Override
    public double getPrecio() {
        return evento.getPrecioBase();
    }
}
