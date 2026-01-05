package vista;

import control.command.Invocador;
import control.command.ModificarEventoCommand;
import controll.CatalogoEventos;
import modelo.eventos.Evento;
import modelo.eventos.EstadoEvento;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentanaEditarEvento extends JFrame {

    private final Evento original;
    private final Invocador invocador;
    private final CatalogoEventos catalogo;
    private final VentanaAdministrador ventanaPadre;

    private JTextField txtNombre, txtFecha, txtLugar, txtPrecio, txtAforo;

    public VentanaEditarEvento(VentanaAdministrador padre, Evento original, Invocador invocador, CatalogoEventos catalogo) {
        this.original = original;
        this.invocador = invocador;
        this.catalogo = catalogo;
        this.ventanaPadre = padre;

        setTitle("Editar: " + original.getNombre());
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Estilos.aplicarEstiloVentana(this);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(Estilos.crearTitulo("Modificar Evento"), BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 10, 20));
        panelForm.setBackground(Estilos.COLOR_FONDO);
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        agregarCampo(panelForm, "Nombre:", txtNombre = new JTextField(original.getNombre()));
        agregarCampo(panelForm, "Fecha (yyyy-MM-dd HH:mm):", txtFecha = new JTextField(original.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        agregarCampo(panelForm, "Lugar:", txtLugar = new JTextField(original.getLugar()));
        agregarCampo(panelForm, "Precio Base:", txtPrecio = new JTextField(String.valueOf(original.getPrecioBase())));
        agregarCampo(panelForm, "Aforo Disponible:", txtAforo = new JTextField(String.valueOf(original.getAforoDisponible())));

        add(panelForm, BorderLayout.CENTER);

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

    private void guardarCambios() {
        try {
            String nombre = txtNombre.getText().trim();
            LocalDateTime fecha = LocalDateTime.parse(txtFecha.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String lugar = txtLugar.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int aforo = Integer.parseInt(txtAforo.getText().trim());

            Evento modificado = original.clonarConNuevosDatos(nombre, fecha, lugar, precio, aforo);
            // Mantenemos el estado e imagen originales para no perderlos
            modificado.setEstado(original.getEstado());
            modificado.setRutaImagen(original.getRutaImagen());

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
