package modelo.entradas;

public class EntradaVIP extends EntradaDecorator {
    public EntradaVIP(Entrada base) { super(base); }

    @Override
    public String getDescripcion() {
        return base.getDescripcion() + " + VIP";
    }

    @Override
    public double getPrecio() {
        return base.getPrecio() + 30.0; // suplemento VIP
    }
}
