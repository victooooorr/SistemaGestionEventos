package vista;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.time.YearMonth;

public class VentanaPasarelaPago extends JDialog {

    private boolean pagoRealizado = false;
    private JTextField txtNumero;
    private JTextField txtCVV;
    private JFormattedTextField txtCaducidad;

    public VentanaPasarelaPago(Frame owner, double monto) {
        super(owner, "Pasarela de Pago Segura", true);
        setSize(400, 260);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel panelDatos = new JPanel(new GridLayout(4, 2, 10, 10));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Importe
        panelDatos.add(new JLabel("Importe a pagar:"));
        JLabel lblMonto = new JLabel(String.format("%.2f €", monto));
        lblMonto.setFont(new Font("Arial", Font.BOLD, 14));
        lblMonto.setForeground(Color.BLUE);
        panelDatos.add(lblMonto);

        // Número de tarjeta
        panelDatos.add(new JLabel("Número de Tarjeta:"));
        txtNumero = new JTextField();
        limitarCampoNumerico(txtNumero, 16);
        panelDatos.add(txtNumero);

        // Fecha caducidad
        panelDatos.add(new JLabel("Fecha Caducidad (MM/YY):"));
        try {
            MaskFormatter formatter = new MaskFormatter("##/##");
            formatter.setPlaceholderCharacter('_');
            txtCaducidad = new JFormattedTextField(formatter);
        } catch (Exception e) {
            txtCaducidad = new JFormattedTextField();
        }
        txtCaducidad.setFocusLostBehavior(JFormattedTextField.COMMIT);
        panelDatos.add(txtCaducidad);

        // CVV
        panelDatos.add(new JLabel("CVV:"));
        txtCVV = new JTextField();
        limitarCampoNumerico(txtCVV, 3);
        panelDatos.add(txtCVV);

        // Botón pagar
        JButton btnPagar = new JButton("Confirmar Pago");
        btnPagar.setBackground(new Color(0, 128, 0));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.addActionListener(e -> validarYPagar());

        add(panelDatos, BorderLayout.CENTER);
        add(btnPagar, BorderLayout.SOUTH);
    }

    // Limita un campo a solo números y máximo X dígitos
    private void limitarCampoNumerico(JTextField campo, int maxDigitos) {
        PlainDocument doc = (PlainDocument) campo.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= maxDigitos && string.matches("\\d+")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() - length + text.length()) <= maxDigitos && text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    private void validarYPagar() {
        try {
            String tarjeta = txtNumero.getText().trim();
            String fecha = txtCaducidad.getText().trim();
            String cvv = txtCVV.getText().trim();

            // Validación número tarjeta
            if (tarjeta.length() != 16)
                throw new IllegalArgumentException("El número de tarjeta debe tener 16 dígitos.");

            // Validación fecha formato
            if (!fecha.matches("(0[1-9]|1[0-2])/\\d{2}"))
                throw new IllegalArgumentException("La fecha debe tener formato MM/YY.");

            // Validación fecha lógica
            String[] partes = fecha.split("/");
            int mes = Integer.parseInt(partes[0]);
            int año = 2000 + Integer.parseInt(partes[1]);
            YearMonth caducidad = YearMonth.of(año, mes);

            if (caducidad.isBefore(YearMonth.now()))
                throw new IllegalArgumentException("La tarjeta está caducada.");

            // Validación CVV
            if (cvv.length() != 3)
                throw new IllegalArgumentException("El CVV debe tener 3 dígitos.");

            // Simulación de pago
            Thread.sleep(800);

            pagoRealizado = true;
            JOptionPane.showMessageDialog(this, "¡Pago procesado correctamente!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public boolean isPagoRealizado() {
        return pagoRealizado;
    }
}
