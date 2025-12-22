package vista;

import controll.CatalogoEventos;
import excepciones.EventoYaExisteException;
import modelo.eventos.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import modelo.usuarios.Administrador;

public class VentanaAdministrador extends JFrame {

    private JComboBox<String> tipoEventoCombo;

    // Campos comunes
    private JTextField codigoField, nombreField, lugarField, aforoField, precioField, urlField;
    private JTextField fechaField, horaField;

    // Panel dinámico
    private JPanel panelEspecifico;

    // Campos específicos
    private JTextField generoField, artistaField, duracionField; // Concierto
    private JTextField companiaField; // Teatro
    private JTextField ponenteField, tematicaField; // Conferencia
    private JTextField horariosField; // Festival

    private JButton crearButton;

    private final CatalogoEventos catalogo;

    private final Administrador admin;

    public VentanaAdministrador(Administrador admin) {
        this.admin = admin;
        this.catalogo = CatalogoEventos.getInstancia();

        setTitle("Panel de Administrador - " + admin.getNombre());
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setJMenuBar(MenuSuperior.crearMenu(this, admin));

        initComponents();
}


    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(10, 2, 5, 5));

        tipoEventoCombo = new JComboBox<>(new String[]{"Concierto", "Teatro", "Conferencia", "Festival"});
        tipoEventoCombo.addActionListener(e -> actualizarCamposEspecificos());

        codigoField = new JTextField();
        nombreField = new JTextField();
        lugarField = new JTextField();
        aforoField = new JTextField();
        precioField = new JTextField();
        urlField = new JTextField();
        fechaField = new JTextField("2025-12-20");
        horaField = new JTextField("20:00");

        form.add(new JLabel("Tipo de evento:"));
        form.add(tipoEventoCombo);

        form.add(new JLabel("Código:"));
        form.add(codigoField);

        form.add(new JLabel("Nombre:"));
        form.add(nombreField);

        form.add(new JLabel("Lugar:"));
        form.add(lugarField);

        form.add(new JLabel("Aforo máximo:"));
        form.add(aforoField);

        form.add(new JLabel("Precio base:"));
        form.add(precioField);

        form.add(new JLabel("URL info:"));
        form.add(urlField);

        form.add(new JLabel("Fecha (YYYY-MM-DD):"));
        form.add(fechaField);

        form.add(new JLabel("Hora (HH:MM):"));
        form.add(horaField);

        // Panel dinámico
        panelEspecifico = new JPanel(new GridLayout(5, 2, 5, 5));
        actualizarCamposEspecificos();

        crearButton = new JButton("Crear evento");
        crearButton.addActionListener(e -> crearEvento());

        add(form, BorderLayout.NORTH);
        add(panelEspecifico, BorderLayout.CENTER);
        add(crearButton, BorderLayout.SOUTH);
        JButton verVentas = new JButton("Ver ventas");
        verVentas.addActionListener(e -> new VentanaVentas(admin).setVisible(true));
        form.add(verVentas);

    }

    private void actualizarCamposEspecificos() {
        panelEspecifico.removeAll();

        String tipo = (String) tipoEventoCombo.getSelectedItem();

        switch (tipo) {
            case "Concierto" -> {
                generoField = new JTextField();
                artistaField = new JTextField();
                duracionField = new JTextField();

                panelEspecifico.add(new JLabel("Género musical:"));
                panelEspecifico.add(generoField);

                panelEspecifico.add(new JLabel("Artista principal:"));
                panelEspecifico.add(artistaField);

                panelEspecifico.add(new JLabel("Duración (min):"));
                panelEspecifico.add(duracionField);
            }

            case "Teatro" -> {
                companiaField = new JTextField();
                duracionField = new JTextField();

                panelEspecifico.add(new JLabel("Compañía:"));
                panelEspecifico.add(companiaField);

                panelEspecifico.add(new JLabel("Duración (min):"));
                panelEspecifico.add(duracionField);
            }

            case "Conferencia" -> {
                ponenteField = new JTextField();
                tematicaField = new JTextField();
                duracionField = new JTextField();

                panelEspecifico.add(new JLabel("Ponente:"));
                panelEspecifico.add(ponenteField);

                panelEspecifico.add(new JLabel("Temática:"));
                panelEspecifico.add(tematicaField);

                panelEspecifico.add(new JLabel("Duración (min):"));
                panelEspecifico.add(duracionField);
            }

            case "Festival" -> {
                horariosField = new JTextField();

                panelEspecifico.add(new JLabel("Horarios (ej: Día1:18-02):"));
                panelEspecifico.add(horariosField);
            }
        }

        panelEspecifico.revalidate();
        panelEspecifico.repaint();
    }

    private void crearEvento() {
        try {
            String tipo = (String) tipoEventoCombo.getSelectedItem();
            String codigo = codigoField.getText();
            String nombre = nombreField.getText();
            String lugar = lugarField.getText();
            int aforo = Integer.parseInt(aforoField.getText());
            double precio = Double.parseDouble(precioField.getText());
            String url = urlField.getText();

            LocalDateTime fechaHora = LocalDateTime.parse(fechaField.getText() + "T" + horaField.getText());

           Evento evento = null;

            switch (tipo) {
                case "Concierto":
                    evento = new Concierto(
                            codigo, nombre, fechaHora, lugar, aforo, precio, url,
                            generoField.getText(), artistaField.getText(),
                            Integer.parseInt(duracionField.getText())
                    );
                    break;

                case "Teatro":
                    evento = new Teatro(
                            codigo, nombre, fechaHora, lugar, aforo, precio, url,
                            companiaField.getText(),
                            Integer.parseInt(duracionField.getText())
                    );
                    break;

                case "Conferencia":
                    evento = new Conferencia(
                            codigo, nombre, fechaHora, lugar, aforo, precio, url,
                            ponenteField.getText(), tematicaField.getText(),
                            Integer.parseInt(duracionField.getText())
                    );
                    break;

                case "Festival":
                    Festival f = new Festival(codigo, nombre, fechaHora, lugar, aforo, precio, url);
                    f.agregarSubevento(new HorarioFestival("Día 1", horariosField.getText()));
                    evento = f;
                    break;
            }


            catalogo.agregarEvento(evento);

            JOptionPane.showMessageDialog(this, "✅ Evento creado correctamente.");

        } catch (EventoYaExisteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear evento: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
