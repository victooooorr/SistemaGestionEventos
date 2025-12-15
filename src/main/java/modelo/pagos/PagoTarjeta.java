package modelo.pagos;

public class PagoTarjeta implements EstrategiaPago {
    @Override
    public void pagar(double monto) {
        System.out.printf("Pago con tarjeta: %.2f€%n", monto);
    }
}
