package modelo.usuarios;

import modelo.eventos.Evento;

public class Preferencias {
    private String tipoEvento;
    private String lugar;
    private double precioMax;

    public Preferencias(String tipoEvento, String lugar, double precioMax) {
        this.tipoEvento = tipoEvento;
        this.lugar = lugar;
        this.precioMax = precioMax;
    }

    public boolean coincide(Evento e) {
        boolean tipoOk = (tipoEvento == null) || tipoEvento.equalsIgnoreCase(e.getTipo());
        boolean lugarOk = (lugar == null) || lugar.equalsIgnoreCase(e.getLugar());
        boolean precioOk = (precioMax <= 0) || e.getPrecioBase() <= precioMax;
        return tipoOk && lugarOk && precioOk;
    }
}
