package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPagoPayPal extends JDialog {

    private boolean pagoRealizado = false;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public VentanaPagoPayPal(Frame owner, double monto) {
        super(owner, "Pago con PayPal", true); // Modal
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Usuario
        panel.add(new JLabel("Usuario PayPal:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        // Contraseña
        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        // Importe
        panel.add(new JLabel("Importe a pagar:"));
        JLabel lblMonto = new JLabel(String.format("%.2f €", monto));
        lblMonto.setFont(new Font("Arial", Font.BOLD, 14));
        lblMonto.setForeground(Color.BLUE);
        panel.add(lblMonto);

        // Botón pagar
        JButton btnPagar = new JButton("Iniciar sesión y pagar");
        btnPagar.setBackground(new Color(0, 102, 204));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.addActionListener(e -> validarYPagar());

        add(panel, BorderLayout.CENTER);
        add(btnPagar, BorderLayout.SOUTH);
    }

    private void validarYPagar() {
        String usuario = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (usuario.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce usuario y contraseña.");
            return;
        }

        // Simulación de login PayPal
        try {
            Thread.sleep(800);
        } catch (InterruptedException ignored) {}

        pagoRealizado = true;
        JOptionPane.showMessageDialog(this, "Pago realizado correctamente con PayPal.");
        dispose();
    }

    public boolean isPagoRealizado() {
        return pagoRealizado;
    }
}
