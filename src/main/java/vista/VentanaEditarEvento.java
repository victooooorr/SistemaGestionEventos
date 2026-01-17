package vista;

import control.command.Invocador;
import control.command.ModificarEventoCommand;
import controll.CatalogoEventos;
import modelo.eventos.Evento;
import modelo.eventos.Festival;
import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class VentanaEditarEvento extends JFrame {

    private final Evento original;
    private final Invocador invocador;
    private final CatalogoEventos catalogo;
    private final VentanaAdministrador ventanaPadre;

    private JTextField txtNombre, txtLugar, txtPrecio, txtAforo;
    private JLabel lblRutaImagen;
    private String rutaImagenActual;

    private JDatePickerImpl datePicker;
    private JSpinner spinnerHora;

    public VentanaEditarEvento(VentanaAdministrador padre, Evento original, Invocador invocador, CatalogoEventos catalogo) {
        this.original = original;
        this.invocador = invocador;
        this.catalogo = catalogo;
        this.ventanaPadre = padre;

        this.rutaImagenActual = original.getRutaImagen();

        setTitle("Editar: " + original.getNombre());
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Estilos.aplicarEstiloVentana(this);

        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(Estilos.crearTitulo("Modificar Evento"), BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 10, 20));
        panelForm.setBackground(Estilos.COLOR_FONDO);
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        agregarCampo(panelForm, "Nombre:", txtNombre = new JTextField());
        agregarCampo(panelForm, "Lugar:", txtLugar = new JTextField());
        agregarCampo(panelForm, "Precio Base:", txtPrecio = new JTextField());
        agregarCampo(panelForm, "Aforo Disponible:", txtAforo = new JTextField());

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
        JPanel panelImg = new JPanel(new BorderLayout(5, 0));
        panelImg.setBackground(Estilos.COLOR_FONDO);

        JButton btnCambiarImg = new JButton("Cambiar...");
        lblRutaImagen = new JLabel("Sin imagen", SwingConstants.CENTER);

        btnCambiarImg.addActionListener(e -> seleccionarNuevaImagen());

        panelImg.add(lblRutaImagen, BorderLayout.CENTER);
        panelImg.add(btnCambiarImg, BorderLayout.EAST);
        panelForm.add(panelImg);

        // --- SUBEVENTOS SI ES FESTIVAL ---
        if (original instanceof Festival festival) {
            JButton btnSubeventos = Estilos.crearBoton("Gestionar Subeventos", Estilos.COLOR_SECUNDARIO);
            btnSubeventos.addActionListener(e -> {
                new VentanaEditarSubeventosFestival(festival, catalogo, ventanaPadre).setVisible(true);
            });
            panelForm.add(new JLabel("Subeventos:"));
            panelForm.add(btnSubeventos);
        }

        add(panelForm, BorderLayout.CENTER);

        // --- BOTONES ---
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Estilos.COLOR_FONDO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton btnGuardar = Estilos.crearBoton("Guardar Cambios", Estilos.COLOR_PRIMARIO);
        JButton btnCancelar = Estilos.crearBoton("Cancelar", Color.GRAY);

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel panel, String texto, JTextField campo) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(Estilos.FONT_BOLD);
        panel.add(lbl);
        panel.add(campo);
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

    private void cargarDatos() {
        txtNombre.setText(original.getNombre());
        txtLugar.setText(original.getLugar());
        txtPrecio.setText(String.valueOf(original.getPrecioBase()));
        txtAforo.setText(String.valueOf(original.getAforoDisponible()));

        lblRutaImagen.setText(obtenerNombreArchivo(rutaImagenActual));

        // Fecha
        LocalDateTime fecha = original.getFechaHora();
        datePicker.getModel().setDate(fecha.getYear(), fecha.getMonthValue() - 1, fecha.getDayOfMonth());
        datePicker.getModel().setSelected(true);

        // Hora
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, fecha.getHour());
        cal.set(Calendar.MINUTE, fecha.getMinute());
        spinnerHora.setValue(cal.getTime());
    }

    private String obtenerNombreArchivo(String ruta) {
        if (ruta == null || ruta.isEmpty()) return "Sin imagen";
        return new File(ruta).getName();
    }

    private void seleccionarNuevaImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            rutaImagenActual = selectedFile.getAbsolutePath();
            lblRutaImagen.setText(selectedFile.getName());
        }
    }

    private void guardarCambios() {
        try {
            String nombre = txtNombre.getText().trim();
            String lugar = txtLugar.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int aforo = Integer.parseInt(txtAforo.getText().trim());

            // Fecha
            Date selected = (Date) datePicker.getModel().getValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una fecha válida.");
                return;
            }

            LocalDate fecha = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Hora
            Date horaDate = (Date) spinnerHora.getValue();
            LocalTime hora = horaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0);

            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

            Evento modificado = original.clonarConNuevosDatos(nombre, fechaHora, lugar, precio, aforo);

            modificado.setEstado(original.getEstado());
            modificado.setRutaImagen(rutaImagenActual);

            invocador.añadir(new ModificarEventoCommand(catalogo, original, modificado));
            invocador.ejecutarTodos();

            JOptionPane.showMessageDialog(this, "Evento modificado correctamente.");
            ventanaPadre.cargarEventos();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }
}

// FORMATTER PARA EL CALENDARIO
class DateLabelFormatter2 extends JFormattedTextField.AbstractFormatter {

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
