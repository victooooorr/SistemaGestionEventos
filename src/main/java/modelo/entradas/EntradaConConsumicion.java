package modelo.entradas;

public class EntradaConConsumicion extends EntradaDecorator {
    public EntradaConConsumicion(Entrada base) { super(base); }

    @Override
    public String getDescripcion() {
        return base.getDescripcion() + " + Consumición";
    }

    @Override
    public double getPrecio() {
        return base.getPrecio() + 6.0; // suplemento consumición
    }
}
