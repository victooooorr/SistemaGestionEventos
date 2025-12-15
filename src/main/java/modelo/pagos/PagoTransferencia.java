package modelo.pagos;

public class PagoTransferencia implements EstrategiaPago {
    @Override
    public void pagar(double monto) {
        System.out.printf("Pago por transferencia: %.2f€%n", monto);
    }
}
