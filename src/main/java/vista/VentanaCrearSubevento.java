package vista;

import modelo.eventos.ComponenteEvento;
import modelo.eventos.Evento;
import modelo.eventos.builder.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentanaCrearSubevento extends JFrame {

    private final VentanaSubeventosFestival padre;

    private JComboBox<String> comboTipo;
    private JTextField txtNombre;
    private JTextField txtFecha;
    private JTextField txtLugar;
    private JTextField txtPrecio;
    private JTextField txtAforo;

    public VentanaCrearSubevento(VentanaSubeventosFestival padre) {
        this.padre = padre;

        setTitle("Crear subevento");
        setSize(350, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Fecha (yyyy-MM-dd HH:mm):"));
        txtFecha = new JTextField();
        panel.add(txtFecha);

        panel.add(new JLabel("Lugar:"));
        txtLugar = new JTextField();
        panel.add(txtLugar);

        panel.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panel.add(txtPrecio);

        panel.add(new JLabel("Aforo:"));
        txtAforo = new JTextField();
        panel.add(txtAforo);

        panel.add(new JLabel("Tipo:"));
        comboTipo = new JComboBox<>(new String[]{"Concierto", "Conferencia", "Teatro"});
        panel.add(comboTipo);

        JButton btnCrear = new JButton("Añadir");
        btnCrear.addActionListener(e -> crearSubevento());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCrear);
        panelBotones.add(btnCancelar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void crearSubevento() {
        try {
            String nombre = txtNombre.getText().trim();
            String fechaStr = txtFecha.getText().trim();
            String lugar = txtLugar.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int aforo = Integer.parseInt(txtAforo.getText().trim());

            LocalDateTime fecha = LocalDateTime.parse(
                    fechaStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );

            String tipo = (String) comboTipo.getSelectedItem();

            EventoBuilder builder;

            switch (tipo) {
                case "Concierto":
                    builder = new ConciertoBuilder();
                    break;

                case "Conferencia":
                    builder = new ConferenciaBuilder();
                    break;

                case "Teatro":
                    builder = new TeatroBuilder();
                    break;

                default:
                    throw new IllegalStateException("Tipo no soportado");
            }

            Evento sub = builder
                    .conCodigo("SUB-" + System.currentTimeMillis())
                    .conNombre(nombre)
                    .conFecha(fecha)
                    .conLugar(lugar)
                    .conPrecio(precio)
                    .conAforo(aforo)
                    .conUrl("N/A")
                    .build();

            padre.agregarSubevento((ComponenteEvento) sub);

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear subevento: " + ex.getMessage());
        }
    }
}
