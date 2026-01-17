package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPagoTransferencia extends JDialog {

    public VentanaPagoTransferencia(Frame owner, double monto) {
        super(owner, "Pago por Transferencia Bancaria", true); // Modal
        setSize(500, 380);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("Instrucciones para Pago por Transferencia");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titulo, BorderLayout.NORTH);

        // Texto explicativo
        JTextArea txtInfo = new JTextArea();
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        txtInfo.setLineWrap(true);
        txtInfo.setWrapStyleWord(true);

        txtInfo.setText(
                "Para completar tu compra mediante transferencia bancaria, realiza el pago a la siguiente cuenta:\n\n" +
                "    IBAN: ES12 3456 7890 1234 5678 9012\n" +
                "    Titular: Gestión de Eventos S.A.\n" +
                "    Concepto: Compra de entradas\n" +
                "    Importe: " + String.format("%.2f €", monto) + "\n\n" +
                "Dispones de un máximo de 2 horas para realizar la transferencia.\n" +
                "Durante ese tiempo, tu entrada permanecerá reservada.\n\n" +
                "Una vez recibamos el pago, te enviaremos la confirmación por correo electrónico."
        );

        JScrollPane scroll = new JScrollPane(txtInfo);
        panel.add(scroll, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        // Botón justificante
        JButton btnJustificante = new JButton("Descargar Justificante");
        btnJustificante.setBackground(new Color(0, 102, 204));
        btnJustificante.setForeground(Color.WHITE);

        btnJustificante.addActionListener(e -> mostrarJustificante(monto));

        // Botón cerrar
        JButton btnCerrar = new JButton("Entendido");
        btnCerrar.setBackground(new Color(0, 128, 0));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnJustificante);
        panelBotones.add(btnCerrar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void mostrarJustificante(double monto) {
        String justificante = """
                JUSTIFICANTE DE RESERVA POR TRANSFERENCIA

                Gracias por tu compra. A continuación se muestran los datos de tu reserva:

                Método de pago: Transferencia bancaria
                Importe: %s €

                Cuenta destino:
                IBAN: ES12 3456 7890 1234 5678 9012
                Titular: Gestión de Eventos S.A.

                Recuerda que dispones de 2 horas para realizar la transferencia.
                Tu entrada permanecerá reservada durante ese tiempo.

                Este justificante no es una confirmación de pago.
                """.formatted(String.format("%.2f", monto));

        JTextArea area = new JTextArea(justificante);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 300));

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Justificante de Reserva",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
