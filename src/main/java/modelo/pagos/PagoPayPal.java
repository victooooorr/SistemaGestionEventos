package modelo.pagos;

public class PagoPayPal implements EstrategiaPago {
    @Override
    public void pagar(double monto) {
        System.out.printf("Pago con PayPal: %.2f€%n", monto);
    }
}
