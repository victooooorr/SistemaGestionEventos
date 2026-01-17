package vista;

import modelo.eventos.ComponenteEvento;
import modelo.eventos.Evento;
import modelo.eventos.builder.*;
import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class VentanaCrearSubevento extends JDialog {

    private final Object padre;

    private JTextField txtNombre, txtLugar, txtPrecio, txtAforo;
    private JComboBox<String> comboTipo;
    private JDatePickerImpl datePicker;
    private JSpinner spinnerHora;

    public VentanaCrearSubevento(Object padre) {
        this.padre = padre;

        setTitle("Nuevo Subevento");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setModal(true);
        Estilos.aplicarEstiloVentana(this);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Estilos.COLOR_FONDO);

        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Lugar:"));
        txtLugar = new JTextField();
        panel.add(txtLugar);

        panel.add(new JLabel("Precio:"));
        txtPrecio = new JTextField("0.0");
        panel.add(txtPrecio);

        panel.add(new JLabel("Aforo:"));
        txtAforo = new JTextField("100");
        panel.add(txtAforo);

        panel.add(new JLabel("Tipo:"));
        comboTipo = new JComboBox<>(new String[]{"Concierto", "Conferencia", "Teatro"});
        panel.add(comboTipo);

        panel.add(new JLabel("Fecha:"));
        datePicker = crearDatePicker();
        panel.add(datePicker);

        panel.add(new JLabel("Hora:"));
        spinnerHora = crearSpinnerHora();
        panel.add(spinnerHora);

        add(panel, BorderLayout.CENTER);

        JButton btnOk = Estilos.crearBoton("Añadir", Estilos.COLOR_PRIMARIO);
        btnOk.addActionListener(e -> crear());
        add(btnOk, BorderLayout.SOUTH);
    }

    private JDatePickerImpl crearDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");

        return new JDatePickerImpl(new JDatePanelImpl(model, p), new DateLabelFormatter());
    }

    private JSpinner crearSpinnerHora() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
        return spinner;
    }

    private void crear() {
        try {
            String nombre = txtNombre.getText();
            String lugar = txtLugar.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int aforo = Integer.parseInt(txtAforo.getText());

            Date selected = (Date) datePicker.getModel().getValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una fecha válida.");
                return;
            }

            LocalDate fecha = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Date horaDate = (Date) spinnerHora.getValue();
            LocalTime hora = horaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0);

            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

            EventoBuilder builder = switch ((String) comboTipo.getSelectedItem()) {
                case "Concierto" -> new ConciertoBuilder();
                case "Conferencia" -> new ConferenciaBuilder();
                default -> new TeatroBuilder();
            };

            Evento sub = builder
                    .conCodigo("SUB-" + System.currentTimeMillis())
                    .conNombre(nombre)
                    .conLugar(lugar)
                    .conPrecio(precio)
                    .conAforo(aforo)
                    .conFecha(fechaHora)
                    .conUrl("")
                    .build();

            if (padre instanceof VentanaSubeventosFestival v1)
                v1.agregarSubevento((ComponenteEvento) sub);

            if (padre instanceof VentanaEditarSubeventosFestival v2)
                v2.agregarSubevento((ComponenteEvento) sub);

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }
}
