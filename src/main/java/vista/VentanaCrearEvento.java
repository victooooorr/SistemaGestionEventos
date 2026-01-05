package vista;

import controll.CatalogoEventos;
import modelo.eventos.Evento;
import modelo.eventos.builder.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentanaCrearEvento extends JFrame {

    private final CatalogoEventos catalogo;
    private final VentanaAdministrador padre;

    private JComboBox<String> comboTipo;
    private JTextField txtCodigo, txtNombre, txtFecha, txtLugar, txtPrecio, txtAforo, txtUrl;
    private JLabel lblRutaImagen;
    private String rutaImagenSeleccionada = "imagenes/default.png"; // Valor por defecto

    public VentanaCrearEvento(VentanaAdministrador padre, CatalogoEventos catalogo) {
        this.catalogo = catalogo;
        this.padre = padre;
        setTitle("Nuevo Evento");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Estilos.aplicarEstiloVentana(this);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(Estilos.crearTitulo("Registrar Evento"), BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 10, 10));
        panelForm.setBackground(Estilos.COLOR_FONDO);
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Campos básicos
        agregarCampo(panelForm, "Tipo Evento:", comboTipo = new JComboBox<>(new String[]{"Concierto", "Conferencia", "Teatro", "Festival"}));
        agregarCampo(panelForm, "Código:", txtCodigo = new JTextField());
        agregarCampo(panelForm, "Nombre:", txtNombre = new JTextField());
        agregarCampo(panelForm, "Fecha (yyyy-MM-dd HH:mm):", txtFecha = new JTextField());
        agregarCampo(panelForm, "Lugar:", txtLugar = new JTextField());
        agregarCampo(panelForm, "Precio Base (€):", txtPrecio = new JTextField());
        agregarCampo(panelForm, "Aforo Máximo:", txtAforo = new JTextField());
        agregarCampo(panelForm, "URL Info:", txtUrl = new JTextField());

        // Selector de Imagen
        panelForm.add(new JLabel("Imagen Cartel:"));
        JPanel panelImg = new JPanel(new BorderLayout());
        panelImg.setBackground(Estilos.COLOR_FONDO);
        
        JButton btnImagen = new JButton("Seleccionar...");
        lblRutaImagen = new JLabel("Defecto", SwingConstants.CENTER);
        
        btnImagen.addActionListener(e -> seleccionarImagen());
        
        panelImg.add(lblRutaImagen, BorderLayout.CENTER);
        panelImg.add(btnImagen, BorderLayout.EAST);
        panelForm.add(panelImg);

        add(panelForm, BorderLayout.CENTER);

        // Botones Acción
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Estilos.COLOR_FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
        
        JButton btnGuardar = Estilos.crearBoton("Guardar Evento", Estilos.COLOR_PRIMARIO);
        JButton btnCancelar = Estilos.crearBoton("Cancelar", Color.GRAY);
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel panel, String label, JComponent comp) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(Estilos.FONT_BOLD);
        panel.add(lbl);
        panel.add(comp);
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            rutaImagenSeleccionada = selectedFile.getAbsolutePath();
            lblRutaImagen.setText(selectedFile.getName());
        }
    }

    private void guardar() {
        try {
            // Recoger datos básicos
            String codigo = txtCodigo.getText();
            String nombre = txtNombre.getText();
            LocalDateTime fecha = LocalDateTime.parse(txtFecha.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String lugar = txtLugar.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int aforo = Integer.parseInt(txtAforo.getText());
            String url = txtUrl.getText();
            String tipo = (String) comboTipo.getSelectedItem();

            // Construcción del evento
            EventoBuilder builder;
            switch (tipo) {
                case "Concierto" -> builder = new ConciertoBuilder();
                case "Conferencia" -> builder = new ConferenciaBuilder();
                case "Teatro" -> builder = new TeatroBuilder();
                case "Festival" -> {
                    // Caso especial Festival
                    FestivalAdapterBuilder fb = new FestivalAdapterBuilder();
                    fb.conCodigo(codigo).conNombre(nombre).conFecha(fecha).conLugar(lugar)
                      .conPrecio(precio).conAforo(aforo).conUrl(url);
                    Evento f = fb.build();
                    f.setRutaImagen(rutaImagenSeleccionada);
                    catalogo.agregarEvento(f);
                    
                    new VentanaSubeventosFestival(fb, catalogo, padre).setVisible(true);
                    dispose();
                    return;
                }
                default -> throw new IllegalStateException("Tipo no válido");
            }

            Evento nuevo = builder
                    .conCodigo(codigo).conNombre(nombre).conFecha(fecha)
                    .conLugar(lugar).conPrecio(precio).conAforo(aforo).conUrl(url)
                    .build();
            
            // ASIGNAR IMAGEN
            nuevo.setRutaImagen(rutaImagenSeleccionada);

            catalogo.agregarEvento(nuevo);
            JOptionPane.showMessageDialog(this, "Evento creado correctamente.");
            padre.cargarEventos();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en los datos: " + ex.getMessage());
        }
    }
}