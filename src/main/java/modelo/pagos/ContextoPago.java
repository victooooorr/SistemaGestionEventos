package modelo.pagos;

public class ContextoPago {
    private EstrategiaPago estrategia;

    public void setEstrategia(EstrategiaPago estrategia) { this.estrategia = estrategia; }

    public void ejecutarPago(double monto) {
        if (estrategia == null) throw new IllegalStateException("No se ha configurado una estrategia de pago.");
        estrategia.pagar(monto);
    }
}
