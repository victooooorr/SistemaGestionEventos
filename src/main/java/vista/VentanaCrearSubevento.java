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
    private JTextField txtNombre, txtFecha, txtLugar, txtPrecio, txtAforo;

    public VentanaCrearSubevento(VentanaSubeventosFestival padre) {
        this.padre = padre;
        setTitle("Nueva Actividad");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Estilos.aplicarEstiloVentana(this);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(Estilos.crearTitulo("Detalles del Subevento"), BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBackground(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        agregarCampo(panel, "Nombre:", txtNombre = new JTextField());
        agregarCampo(panel, "Fecha (yyyy-MM-dd HH:mm):", txtFecha = new JTextField());
        agregarCampo(panel, "Lugar:", txtLugar = new JTextField());
        agregarCampo(panel, "Precio:", txtPrecio = new JTextField("0.0"));
        agregarCampo(panel, "Aforo:", txtAforo = new JTextField("100"));
        
        panel.add(new JLabel("Tipo:"));
        comboTipo = new JComboBox<>(new String[]{"Concierto", "Conferencia", "Teatro"});
        panel.add(comboTipo);

        add(panel, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        botones.setBackground(Estilos.COLOR_FONDO);
        JButton btnOk = Estilos.crearBoton("Añadir", Estilos.COLOR_PRIMARIO);
        btnOk.addActionListener(e -> crear());
        botones.add(btnOk);
        
        add(botones, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel p, String l, JTextField t) {
        JLabel lbl = new JLabel(l);
        lbl.setFont(Estilos.FONT_BOLD);
        p.add(lbl);
        p.add(t);
    }

    private void crear() {
        try {
            String nombre = txtNombre.getText();
            LocalDateTime fecha = LocalDateTime.parse(txtFecha.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            
            EventoBuilder builder = switch ((String) comboTipo.getSelectedItem()) {
                case "Concierto" -> new ConciertoBuilder();
                case "Conferencia" -> new ConferenciaBuilder();
                default -> new TeatroBuilder();
            };
            
            Evento sub = builder.conCodigo("SUB-" + System.currentTimeMillis())
                    .conNombre(nombre).conFecha(fecha).conLugar(txtLugar.getText())
                    .conPrecio(Double.parseDouble(txtPrecio.getText()))
                    .conAforo(Integer.parseInt(txtAforo.getText()))
                    .conUrl("").build();
            
            padre.agregarSubevento((ComponenteEvento) sub);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }
}
