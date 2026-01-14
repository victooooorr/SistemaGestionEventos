package vista;

import control.command.EliminarEventoCommand;
import control.command.Invocador;
import control.observer.Observador;
import controll.CatalogoEventos;
import controll.GestorNotificaciones;
import controll.GestorUsuarios;
import modelo.eventos.EstadoEvento;
import modelo.eventos.Evento;
import modelo.usuarios.Administrador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaAdministrador extends JFrame implements Observador {

    private final CatalogoEventos catalogo;
    private final Administrador admin;
    private final Invocador invocador = new Invocador();
    
    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;
    private JTextArea areaNotificaciones;

    // --- NUEVO: Componente para mostrar la imagen ---
    private JLabel lblImagenPreview;

    public VentanaAdministrador(Administrador admin) {
        this.admin = admin;
        this.catalogo = CatalogoEventos.getInstancia();
        
        setTitle("Panel de Control - " + admin.getNombre());
        setSize(1100, 750); // Un poco más alto para que quepa bien la foto
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Estilos.aplicarEstiloVentana(this);

        initComponents();
        cargarEventos();
    }

    @Override
    public void actualizar(Evento e) { cargarEventos(); }
    
    @Override
    public void actualizarMensaje(String mensaje, Evento e) {
        areaNotificaciones.append("🔔 " + mensaje + "\n");
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Estilos.COLOR_FONDO);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        
        header.add(Estilos.crearTitulo("Gestión de Eventos"), BorderLayout.WEST);

        JButton btnSalir = Estilos.crearBoton("Cerrar Sesión", Color.GRAY);
        btnSalir.addActionListener(e -> {
            dispose();
            new VentanaLogin(GestorUsuarios.getInstancia()).setVisible(true);
        });
        header.add(btnSalir, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);

        // --- TABLA CENTRAL ---
        modeloTabla = new DefaultTableModel(
            new Object[]{"Código", "Nombre", "Estado", "Fecha", "Lugar", "Aforo", "Precio"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaEventos = new JTable(modeloTabla);
        Estilos.estilizarTabla(tablaEventos);
        
        // --- NUEVO: Listener para detectar click en la tabla ---
        tablaEventos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarFotoSeleccionada();
            }
        });
        
        JScrollPane scrollTabla = new JScrollPane(tablaEventos);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        scrollTabla.getViewport().setBackground(Color.WHITE);
        
        add(scrollTabla, BorderLayout.CENTER);

        // --- BARRA LATERAL (BOTONES E IMAGEN) ---
        JPanel panelLateral = new JPanel();
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBackground(Estilos.COLOR_FONDO);
        panelLateral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        // 1. VISOR DE IMAGEN
        lblImagenPreview = new JLabel();
        lblImagenPreview.setPreferredSize(new Dimension(260, 180));
        lblImagenPreview.setMaximumSize(new Dimension(260, 180));
        lblImagenPreview.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblImagenPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagenPreview.setText("Selecciona un evento");
        lblImagenPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelLateral.add(lblImagenPreview);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 20))); // Espacio

        // 2. BOTONES
        JButton btnCrear = Estilos.crearBoton("➕ Nuevo Evento", Estilos.COLOR_PRIMARIO);
        JButton btnEditar = Estilos.crearBoton("✏️ Editar", Estilos.COLOR_SECUNDARIO);
        JButton btnEstado = Estilos.crearBoton("🔄 Cambiar Estado", Color.ORANGE);
        JButton btnEliminar = Estilos.crearBoton("🗑️ Eliminar", Estilos.COLOR_PELIGRO);
        
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(260, 10));

        JButton btnVentasEvento = Estilos.crearBoton("🎟️ Ventas Evento", new Color(0, 153, 153));
        JButton btnVentasGlobal = Estilos.crearBoton("💰 Ventas Totales", new Color(46, 204, 113));

        Dimension btnSize = new Dimension(260, 45); 
        
        for (JComponent c : new JComponent[]{btnCrear, btnEditar, btnEstado, btnEliminar, sep, btnVentasEvento, btnVentasGlobal}) {
            c.setMaximumSize(btnSize);
            c.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelLateral.add(c);
            panelLateral.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        // Listeners Botones
        btnCrear.addActionListener(e -> new VentanaCrearEvento(this, catalogo).setVisible(true));
        btnEditar.addActionListener(e -> modificarEvento());
        btnEstado.addActionListener(e -> cambiarEstado());
        btnEliminar.addActionListener(e -> eliminarEvento());
        btnVentasEvento.addActionListener(e -> verVentasDeEvento());
        btnVentasGlobal.addActionListener(e -> new VentanaVentas(admin).setVisible(true));

        add(panelLateral, BorderLayout.EAST);

        // --- PIE (LOG) ---
        areaNotificaciones = new JTextArea(5, 20);
        areaNotificaciones.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(areaNotificaciones);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Registro de Actividad"));
        
        for(String n : GestorNotificaciones.obtener()) areaNotificaciones.append(n + "\n");
        
        add(scrollLog, BorderLayout.SOUTH);
    }

    public void cargarEventos() {
        modeloTabla.setRowCount(0);
        for (Evento e : catalogo.listarEventos()) {
            if (!e.getObservadores().contains(this)) e.agregarObservador(this);
            modeloTabla.addRow(new Object[]{
                e.getCodigo(), e.getNombre(), e.getEstado(), 
                e.getFechaHora(), e.getLugar(), e.getAforoDisponible(), e.getPrecioBase()
            });
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private String obtenerCodigoSeleccionado() {
        String codigo = obtenerCodigoSeleccionadoSinAviso();
        if (codigo == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un evento de la lista.");
        }
        return codigo;
    }

    // Método silencioso para el listener de la tabla (no muestra popup)
    private String obtenerCodigoSeleccionadoSinAviso() {
        int fila = tablaEventos.getSelectedRow();
        if (fila == -1) return null;
        return (String) modeloTabla.getValueAt(fila, 0);
    }

    // --- NUEVO: Lógica para cargar y mostrar la imagen ---
    private void actualizarFotoSeleccionada() {
        String codigo = obtenerCodigoSeleccionadoSinAviso();
        if (codigo != null) {
            Evento evento = catalogo.buscarEvento(codigo);
            String ruta = evento.getRutaImagen();
            
            ImageIcon icono = null;
            try {
                // Opción A: Es URL Web
                if (ruta != null && (ruta.startsWith("http://") || ruta.startsWith("https://"))) {
                    icono = new ImageIcon(new java.net.URL(ruta));
                } 
                // Opción B: Es archivo local
                else if (ruta != null && !ruta.isEmpty()) {
                    icono = new ImageIcon(ruta);
                    // Si falla carga local, intentar como recurso del proyecto
                    if (icono.getImageLoadStatus() != MediaTracker.COMPLETE) {
                         java.net.URL resourceUrl = getClass().getResource(ruta);
                         if (resourceUrl != null) icono = new ImageIcon(resourceUrl);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error cargando imagen: " + e.getMessage());
            }

            // Redimensionar y mostrar
            if (icono != null && icono.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image img = icono.getImage();
                Image newImg = img.getScaledInstance(260, 180, Image.SCALE_SMOOTH);
                lblImagenPreview.setText("");
                lblImagenPreview.setIcon(new ImageIcon(newImg));
            } else {
                lblImagenPreview.setIcon(null);
                lblImagenPreview.setText("Sin imagen");
            }
        }
    }
    
    // --- MÉTODOS DE ACCIÓN ---
    
    private void verVentasDeEvento() {
        String codigo = obtenerCodigoSeleccionado();
        if (codigo != null) {
            Evento e = catalogo.buscarEvento(codigo);
            new VentanaVentas(admin, e).setVisible(true);
        }
    }

    private void eliminarEvento() {
        String codigo = obtenerCodigoSeleccionado();
        if (codigo != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar " + codigo + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                invocador.añadir(new EliminarEventoCommand(catalogo, codigo));
                invocador.ejecutarTodos();
                cargarEventos();
                // Limpiar la foto al eliminar
                lblImagenPreview.setIcon(null);
                lblImagenPreview.setText("Selecciona un evento");
            }
        }
    }
    
    private void cambiarEstado() {
        String codigo = obtenerCodigoSeleccionado();
        if (codigo != null) {
            Evento e = catalogo.buscarEvento(codigo);
            EstadoEvento nuevo = (EstadoEvento) JOptionPane.showInputDialog(
                this, "Nuevo estado:", "Gestionar Estado", 
                JOptionPane.QUESTION_MESSAGE, null, EstadoEvento.values(), e.getEstado());
            if (nuevo != null) {
                e.setEstado(nuevo);
                cargarEventos();
            }
        }
    }

    private void modificarEvento() {
        String codigo = obtenerCodigoSeleccionado();
        if (codigo != null) {
            // Nota: Al volver de editar, la foto se actualizará si recargas la tabla
            new VentanaEditarEvento(this, catalogo.buscarEvento(codigo), invocador, catalogo).setVisible(true);
        }
    }
}