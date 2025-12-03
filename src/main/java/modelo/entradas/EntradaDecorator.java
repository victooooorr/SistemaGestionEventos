package modelo.entradas;

public abstract class EntradaDecorator implements Entrada {
    protected Entrada base;

    protected EntradaDecorator(Entrada base) { this.base = base; }
}
