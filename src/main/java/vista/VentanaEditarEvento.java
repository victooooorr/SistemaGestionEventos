package vista;

import control.command.Invocador;
import control.command.ModificarEventoCommand;

import controll.CatalogoEventos;

import modelo.eventos.Evento;
import modelo.eventos.Concierto;
import modelo.eventos.Festival;
import modelo.eventos.Conferencia;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentanaEditarEvento extends JFrame {

    private final Evento original;
    private final Invocador invocador;
    private final CatalogoEventos catalogo;
    private final VentanaAdministrador ventanaPadre;

    private JTextField txtNombre;
    private JTextField txtFecha;
    private JTextField txtLugar;
    private JTextField txtPrecio;
    private JTextField txtAforo;

    public VentanaEditarEvento(VentanaAdministrador padre, Evento original, Invocador invocador, CatalogoEventos catalogo) {
        this.original = original;
        this.invocador = invocador;
        this.catalogo = catalogo;
        this.ventanaPadre = padre;

        setTitle("Modificar evento: " + original.getNombre());
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField(original.getNombre());
        panel.add(txtNombre);

        panel.add(new JLabel("Fecha (yyyy-MM-dd HH:mm):"));
        txtFecha = new JTextField(original.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        panel.add(txtFecha);

        panel.add(new JLabel("Lugar:"));
        txtLugar = new JTextField(original.getLugar());
        panel.add(txtLugar);

        panel.add(new JLabel("Precio base:"));
        txtPrecio = new JTextField(String.valueOf(original.getPrecioBase()));
        panel.add(txtPrecio);

        panel.add(new JLabel("Aforo disponible:"));
        txtAforo = new JTextField(String.valueOf(original.getAforoDisponible()));
        panel.add(txtAforo);

        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void guardarCambios() {
        try {
            String nombre = txtNombre.getText().trim();
            String fechaStr = txtFecha.getText().trim();
            String lugar = txtLugar.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int aforo = Integer.parseInt(txtAforo.getText().trim());

            if (nombre.isEmpty() || fechaStr.isEmpty() || lugar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos.");
                return;
            }

            LocalDateTime fecha = LocalDateTime.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            Evento modificado = original.clonarConNuevosDatos(nombre, fecha, lugar, precio, aforo);


            

            invocador.añadir(new ModificarEventoCommand(catalogo, original, modificado));
            invocador.ejecutarTodos();

            JOptionPane.showMessageDialog(this, "Evento modificado correctamente.");

            ventanaPadre.cargarEventos();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar el evento: " + ex.getMessage());
        }
    }
}
