package vista;

import controll.CatalogoEventos;
import modelo.eventos.Evento;
import modelo.eventos.builder.*;
import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class VentanaCrearEvento extends JFrame {

    private final CatalogoEventos catalogo;
    private final VentanaAdministrador padre;

    private JComboBox<String> comboTipo;
    private JTextField txtCodigo, txtNombre, txtLugar, txtPrecio, txtAforo, txtUrl;
    private JLabel lblRutaImagen;
    private String rutaImagenSeleccionada = "imagenes/default.png";

    private JDatePickerImpl datePicker;
    private JSpinner spinnerHora;

    public VentanaCrearEvento(VentanaAdministrador padre, CatalogoEventos catalogo) {
        this.catalogo = catalogo;
        this.padre = padre;
        setTitle("Nuevo Evento");
        setSize(500, 650);
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

        agregarCampo(panelForm, "Tipo Evento:", comboTipo = new JComboBox<>(new String[]{"Concierto", "Conferencia", "Teatro", "Festival"}));
        agregarCampo(panelForm, "Código:", txtCodigo = new JTextField());
        agregarCampo(panelForm, "Nombre:", txtNombre = new JTextField());
        agregarCampo(panelForm, "Lugar:", txtLugar = new JTextField());
        agregarCampo(panelForm, "Precio Base (€):", txtPrecio = new JTextField());
        agregarCampo(panelForm, "Aforo Máximo:", txtAforo = new JTextField());
        agregarCampo(panelForm, "URL Info:", txtUrl = new JTextField());

        // --- CALENDARIO ---
        panelForm.add(new JLabel("Fecha del Evento:"));
        datePicker = crearDatePicker();
        panelForm.add(datePicker);

        // --- HORA LIBRE ---
        panelForm.add(new JLabel("Hora del Evento:"));
        spinnerHora = crearSpinnerHora();
        panelForm.add(spinnerHora);

        // --- IMAGEN ---
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

        // --- BOTONES ---
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Estilos.COLOR_FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

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

    private JDatePickerImpl crearDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private JSpinner crearSpinnerHora() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm");
        spinner.setEditor(editor);
        return spinner;
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
            String codigo = txtCodigo.getText();
            String nombre = txtNombre.getText();
            String lugar = txtLugar.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int aforo = Integer.parseInt(txtAforo.getText());
            String url = txtUrl.getText();
            String tipo = (String) comboTipo.getSelectedItem();

            // --- FECHA ---
            Date selected = (Date) datePicker.getModel().getValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una fecha válida.");
                return;
            }

            LocalDate fecha = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // --- HORA ---
            Date horaDate = (Date) spinnerHora.getValue();
            LocalTime hora = horaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0);

            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

            // --- BUILDER ---
            EventoBuilder builder;

            switch (tipo) {
                case "Concierto" -> builder = new ConciertoBuilder();
                case "Conferencia" -> builder = new ConferenciaBuilder();
                case "Teatro" -> builder = new TeatroBuilder();
                case "Festival" -> {
                    FestivalAdapterBuilder fb = new FestivalAdapterBuilder();
                    fb.conCodigo(codigo).conNombre(nombre).conFecha(fechaHora).conLugar(lugar)
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
                    .conCodigo(codigo)
                    .conNombre(nombre)
                    .conFecha(fechaHora)
                    .conLugar(lugar)
                    .conPrecio(precio)
                    .conAforo(aforo)
                    .conUrl(url)
                    .build();

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

// FORMATTER PARA EL CALENDARIO
class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

    private final String pattern = "yyyy-MM-dd";
    private final java.text.SimpleDateFormat dateFormatter = new java.text.SimpleDateFormat(pattern);

    @Override
    public Object stringToValue(String text) throws java.text.ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
        return "";
    }
}
