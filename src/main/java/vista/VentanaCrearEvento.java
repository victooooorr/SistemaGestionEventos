package vista;

import controll.CatalogoEventos;
import modelo.eventos.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentanaCrearEvento extends JFrame {

    private final CatalogoEventos catalogo;
    private final VentanaAdministrador padre;

    private JComboBox<String> comboTipo;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtFecha;
    private JTextField txtLugar;
    private JTextField txtPrecio;
    private JTextField txtAforo;
    private JTextField txtUrl;

    public VentanaCrearEvento(VentanaAdministrador padre, CatalogoEventos catalogo) {
        this.catalogo = catalogo;
        this.padre = padre;

        setTitle("Crear nuevo evento");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        panel.add(txtCodigo);

        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Fecha (yyyy-MM-dd HH:mm):"));
        txtFecha = new JTextField();
        panel.add(txtFecha);

        panel.add(new JLabel("Lugar:"));
        txtLugar = new JTextField();
        panel.add(txtLugar);

        panel.add(new JLabel("Precio base:"));
        txtPrecio = new JTextField();
        panel.add(txtPrecio);

        panel.add(new JLabel("Aforo máximo:"));
        txtAforo = new JTextField();
        panel.add(txtAforo);

        panel.add(new JLabel("URL info:"));
        txtUrl = new JTextField();
        panel.add(txtUrl);

        panel.add(new JLabel("Tipo de evento:"));
        comboTipo = new JComboBox<>(new String[]{"Concierto", "Conferencia", "Festival", "Teatro"});
        panel.add(comboTipo);

        JButton btnCrear = new JButton("Crear");
        btnCrear.addActionListener(e -> crearEvento());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCrear);
        panelBotones.add(btnCancelar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void crearEvento() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String fechaStr = txtFecha.getText().trim();
            String lugar = txtLugar.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int aforo = Integer.parseInt(txtAforo.getText().trim());
            String url = txtUrl.getText().trim();

            LocalDateTime fecha = LocalDateTime.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            String tipo = (String) comboTipo.getSelectedItem();

            Evento nuevo;

            switch (tipo) {
                case "Concierto":
                    nuevo = new Concierto(codigo, nombre, fecha, lugar, aforo, precio, url,
                            "Desconocido", "Artista", 90);
                    break;

                case "Conferencia":
                    nuevo = new Conferencia(codigo, nombre, fecha, lugar, aforo, precio, url,
                            "Ponente", "Tema", 60);
                    break;

                case "Festival":
                    nuevo = new Festival(codigo, nombre, fecha, lugar, aforo, precio, url);
                    break;

                case "Teatro":
                    nuevo = new Teatro(codigo, nombre, fecha, lugar, aforo, precio, url,
                            "Compañía", 120);
                    break;

                default:
                    throw new IllegalStateException("Tipo no soportado");
            }

            catalogo.agregarEvento(nuevo);

            JOptionPane.showMessageDialog(this, "Evento creado correctamente.");

            padre.cargarEventos();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear evento: " + ex.getMessage());
        }
    }
}

