package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPasarelaPago extends JDialog {

    private boolean pagoRealizado = false;
    private JTextField txtNumero;
    private JTextField txtCVV;
    private JTextField txtCaducidad;

    public VentanaPasarelaPago(Frame owner, double monto) {
        super(owner, "Pasarela de Pago Segura", true); // true = modal (bloquea la ventana de atrás)
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel panelDatos = new JPanel(new GridLayout(4, 2, 10, 10));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelDatos.add(new JLabel("Importe a pagar:"));
        JLabel lblMonto = new JLabel(String.format("%.2f €", monto));
        lblMonto.setFont(new Font("Arial", Font.BOLD, 14));
        lblMonto.setForeground(Color.BLUE);
        panelDatos.add(lblMonto);

        panelDatos.add(new JLabel("Número de Tarjeta:"));
        txtNumero = new JTextField();
        panelDatos.add(txtNumero);

        panelDatos.add(new JLabel("Fecha Caducidad (MM/YY):"));
        txtCaducidad = new JTextField();
        panelDatos.add(txtCaducidad);

        panelDatos.add(new JLabel("CVV:"));
        txtCVV = new JTextField();
        panelDatos.add(txtCVV);

        JButton btnPagar = new JButton("Confirmar Pago");
        btnPagar.setBackground(new Color(0, 128, 0));
        btnPagar.setForeground(Color.WHITE);
        
        btnPagar.addActionListener(e -> validarYPagar());

        add(panelDatos, BorderLayout.CENTER);
        add(btnPagar, BorderLayout.SOUTH);
    }

    private void validarYPagar() {
        if (txtNumero.getText().isEmpty() || txtCVV.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce los datos de la tarjeta.");
            return;
        }
        // Simulación de proceso
        try {
            Thread.sleep(1000); // Simula conexión con banco
        } catch (InterruptedException e) {}

        pagoRealizado = true;
        JOptionPane.showMessageDialog(this, "¡Pago procesado correctamente!");
        dispose();
    }

    public boolean isPagoRealizado() {
        return pagoRealizado;
    }
}