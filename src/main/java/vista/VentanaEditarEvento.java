package vista;

import control.command.Invocador;
import control.command.ModificarEventoCommand;
import controll.CatalogoEventos;
import modelo.eventos.Evento;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentanaEditarEvento extends JFrame {

    private final Evento original;
    private final Invocador invocador;
    private final CatalogoEventos catalogo;
    private final VentanaAdministrador ventanaPadre;

    private JTextField txtNombre, txtFecha, txtLugar, txtPrecio, txtAforo;
    // --- NUEVOS COMPONENTES IMAGEN ---
    private JLabel lblRutaImagen;
    private String rutaImagenActual;

    public VentanaEditarEvento(VentanaAdministrador padre, Evento original, Invocador invocador, CatalogoEventos catalogo) {
        this.original = original;
        this.invocador = invocador;
        this.catalogo = catalogo;
        this.ventanaPadre = padre;
        
        // Inicializamos la ruta con la que ya tenga el evento
        this.rutaImagenActual = original.getRutaImagen();

        setTitle("Editar: " + original.getNombre());
        setSize(500, 550); // Un poco más alto para el selector de imagen
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

        // --- SELECTOR DE IMAGEN EN EDICIÓN ---
        panelForm.add(new JLabel("Imagen Cartel:"));
        JPanel panelImg = new JPanel(new BorderLayout(5, 0));
        panelImg.setBackground(Estilos.COLOR_FONDO);
        
        JButton btnCambiarImg = new JButton("Cambiar...");
        lblRutaImagen = new JLabel(obtenerNombreArchivo(rutaImagenActual), SwingConstants.CENTER);
        
        btnCambiarImg.addActionListener(e -> seleccionarNuevaImagen());
        
        panelImg.add(lblRutaImagen, BorderLayout.CENTER);
        panelImg.add(btnCambiarImg, BorderLayout.EAST);
        panelForm.add(panelImg);
        // -------------------------------------

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
    
    // Método auxiliar para mostrar solo el nombre del archivo y no toda la ruta larga
    private String obtenerNombreArchivo(String ruta) {
        if (ruta == null || ruta.isEmpty()) return "Sin imagen";
        File f = new File(ruta);
        return f.getName();
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
            LocalDateTime fecha = LocalDateTime.parse(txtFecha.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String lugar = txtLugar.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int aforo = Integer.parseInt(txtAforo.getText().trim());

            Evento modificado = original.clonarConNuevosDatos(nombre, fecha, lugar, precio, aforo);
            
            // Mantenemos el estado original
            modificado.setEstado(original.getEstado());
            // Guardamos la NUEVA ruta de imagen (o la que ya tenía si no se cambió)
            modificado.setRutaImagen(rutaImagenActual);

            invocador.añadir(new ModificarEventoCommand(catalogo, original, modificado));
            invocador.ejecutarTodos();

            JOptionPane.showMessageDialog(this, "Evento modificado correctamente.");
            ventanaPadre.cargarEventos(); // Esto refrescará la tabla y la foto en el admin
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }
}
