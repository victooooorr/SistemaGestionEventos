package vista;

import control.observer.Observador;
import controll.CatalogoEventos;
import controll.GestorEntradas;
import controll.GestorNotificaciones;
import controll.GestorUsuarios;
import modelo.eventos.Evento;
import modelo.eventos.Festival;
import modelo.usuarios.Cliente;
import modelo.entradas.Entrada;
import modelo.entradas.factory.FabricaEntradas;
import modelo.pagos.ContextoPago;
import modelo.pagos.PagoTarjeta;
import modelo.pagos.PagoTransferencia;
import modelo.pagos.PagoPayPal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;

public class VentanaCliente extends JFrame implements Observador {

    private final Cliente cliente;
    private final CatalogoEventos catalogo;
    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;

    // Componentes
    private JComboBox<String> comboMetodoPago;
    private JComboBox<String> comboTipoEntrada;
    private JSpinner spinnerCantidad;
    private JTextField txtBuscador;
    private JComboBox<String> comboFiltroTipo;
    private JTextField txtFiltroFecha;
    private JCheckBox chkNotificaciones;
    private JTextArea areaNotificaciones;
    
    // --- NUEVO: Visor de Imagen ---
    private JLabel lblImagenPreview;

    public VentanaCliente(Cliente cliente) {
        this.cliente = cliente;
        this.catalogo = CatalogoEventos.getInstancia();
        setTitle("Portal de Eventos - Hola " + cliente.getNombre());
        setSize(1200, 800); // Un poco más alto
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Estilos.aplicarEstiloVentana(this);

        initComponents();
    }

    @Override
    public void actualizar(Evento e) {
        cargarEventos(catalogo.listarEventos());
        if (cliente.isRecibeNotificaciones()) {
            areaNotificaciones.append("ℹ️ Cambio en evento: " + e.getNombre() + "\n");
        }
    }
    
    @Override
    public void actualizarMensaje(String mensaje, Evento e) {
         if (cliente.isRecibeNotificaciones()) {
            areaNotificaciones.append("🔔 " + mensaje + "\n");
         }
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // --- 1. PANEL SUPERIOR (HEADER + FILTROS) ---
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(Estilos.COLOR_FONDO);
        panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Título y Logout
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Estilos.COLOR_FONDO);
        header.add(Estilos.crearTitulo("Cartelera de Eventos"), BorderLayout.WEST);
        
        JButton btnSalir = Estilos.crearBoton("Cerrar Sesión", Color.GRAY);
        btnSalir.addActionListener(e -> {
            dispose();
            new VentanaLogin(GestorUsuarios.getInstancia()).setVisible(true);
        });
        header.add(btnSalir, BorderLayout.EAST);
        panelNorte.add(header, BorderLayout.NORTH);

        // Barra de Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        panelFiltros.add(new JLabel(" Buscar:"));
        txtBuscador = new JTextField(15);
        panelFiltros.add(txtBuscador);

        panelFiltros.add(new JLabel(" Tipo:"));
        comboFiltroTipo = new JComboBox<>(new String[]{"Todos", "Concierto", "Teatro", "Conferencia", "Festival"});
        panelFiltros.add(comboFiltroTipo);

        panelFiltros.add(new JLabel(" Fecha (YYYY-MM-DD):"));
        txtFiltroFecha = new JTextField(10);
        panelFiltros.add(txtFiltroFecha);

        JButton btnBuscar = Estilos.crearBoton("Filtrar", Estilos.COLOR_SECUNDARIO);
        btnBuscar.setPreferredSize(new Dimension(100, 30)); 
        btnBuscar.addActionListener(e -> filtrarEventos());
        panelFiltros.add(btnBuscar);
        
        chkNotificaciones = new JCheckBox("Recibir Notificaciones");
        chkNotificaciones.setBackground(Color.WHITE);
        chkNotificaciones.setSelected(cliente.isRecibeNotificaciones());
        chkNotificaciones.addActionListener(e -> cliente.setRecibeNotificaciones(chkNotificaciones.isSelected()));
        panelFiltros.add(chkNotificaciones);

        panelNorte.add(panelFiltros, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        // --- 2. TABLA CENTRAL ---
        modeloTabla = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Estado", "Fecha", "Lugar", "Precio Base", "Aforo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaEventos = new JTable(modeloTabla);
        Estilos.estilizarTabla(tablaEventos);
        
        // Listener selección tabla para cambiar la foto
        tablaEventos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarFotoSeleccionada();
            }
        });
        
        JScrollPane scrollTabla = new JScrollPane(tablaEventos);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        add(scrollTabla, BorderLayout.CENTER);

        // --- 3. PANEL LATERAL (COMPRA E IMAGEN) ---
        JPanel panelLateral = new JPanel();
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBackground(Color.WHITE);
        panelLateral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelLateral.setPreferredSize(new Dimension(300, 0)); // Un poco más ancho para la foto

        // 3.1 VISOR DE IMAGEN
        lblImagenPreview = new JLabel();
        lblImagenPreview.setPreferredSize(new Dimension(260, 180));
        lblImagenPreview.setMaximumSize(new Dimension(260, 180));
        lblImagenPreview.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblImagenPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagenPreview.setText("Selecciona un evento");
        lblImagenPreview.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelLateral.add(lblImagenPreview);
        panelLateral.add(Box.createVerticalStrut(20));

        JLabel lblCompra = new JLabel("Panel de Compra");
        lblCompra.setFont(Estilos.FONT_SUBTITULO);
        lblCompra.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        comboTipoEntrada = new JComboBox<>(new String[]{"Básica", "VIP", "Premium"});
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        comboMetodoPago = new JComboBox<>(new String[]{"Tarjeta", "Transferencia", "PayPal"});
        
        JButton btnComprar = Estilos.crearBoton("Comprar Ahora", Estilos.COLOR_EXITO);
        JButton btnVerHorarios = Estilos.crearBoton("Ver Horarios", Estilos.COLOR_PRIMARIO);
        JButton btnMisTickets = Estilos.crearBoton("Mis Tickets", Estilos.COLOR_SECUNDARIO);

        // Añadir elementos al panel lateral
        panelLateral.add(lblCompra);
        panelLateral.add(Box.createVerticalStrut(20));
        agregarControlLateral(panelLateral, "Tipo de Entrada:", comboTipoEntrada);
        agregarControlLateral(panelLateral, "Cantidad:", spinnerCantidad);
        agregarControlLateral(panelLateral, "Método de Pago:", comboMetodoPago);
        panelLateral.add(Box.createVerticalStrut(20));
        
        btnComprar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnComprar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelLateral.add(btnComprar);
        
        panelLateral.add(Box.createVerticalStrut(10));
        panelLateral.add(new JSeparator());
        panelLateral.add(Box.createVerticalStrut(10));
        
        btnVerHorarios.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVerHorarios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelLateral.add(btnVerHorarios);
        
        panelLateral.add(Box.createVerticalStrut(10));
        
        btnMisTickets.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnMisTickets.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelLateral.add(btnMisTickets);

        add(panelLateral, BorderLayout.EAST);

        // --- 4. PANEL INFERIOR (NOTIFICACIONES) ---
        areaNotificaciones = new JTextArea(4, 50);
        areaNotificaciones.setEditable(false);
        areaNotificaciones.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollNoti = new JScrollPane(areaNotificaciones);
        scrollNoti.setBorder(BorderFactory.createTitledBorder("Centro de Mensajes"));
        scrollNoti.setPreferredSize(new Dimension(0, 100));
        
        add(scrollNoti, BorderLayout.SOUTH);

        // --- LISTENERS ---
        btnComprar.addActionListener(e -> comprarEntrada());
        btnVerHorarios.addActionListener(e -> verHorariosFestival());
        btnMisTickets.addActionListener(e -> new VentanaTickets(cliente).setVisible(true));

        cargarEventos(catalogo.listarEventos());
        
        // Cargar notificaciones históricas
        for (String n : GestorNotificaciones.obtener()) {
            if(cliente.isRecibeNotificaciones()) areaNotificaciones.append(n + "\n");
        }
    }

    private void agregarControlLateral(JPanel panel, String label, JComponent comp) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(Estilos.FONT_BOLD);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
        panel.add(comp);
        panel.add(Box.createVerticalStrut(15));
    }

    private void filtrarEventos() {
        String texto = txtBuscador.getText();
        String tipo = (String) comboFiltroTipo.getSelectedItem();
        LocalDate fecha = null;
        try {
            if (!txtFiltroFecha.getText().isEmpty()) {
                fecha = LocalDate.parse(txtFiltroFecha.getText());
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa YYYY-MM-DD");
            return;
        }
        cargarEventos(catalogo.buscarEventosAvanzado(texto, tipo, fecha));
    }

    private void cargarEventos(Collection<Evento> eventos) {
        modeloTabla.setRowCount(0);
        for (Evento e : eventos) {
            if (!e.getObservadores().contains(this)) {
                e.agregarObservador(this);
            }
            modeloTabla.addRow(new Object[]{
                    e.getCodigo(), e.getNombre(), e.getEstado(), e.getFechaHora(),
                    e.getLugar(), e.getPrecioBase(), e.getAforoDisponible()
            });
        }
    }

    // --- LÓGICA DE CARGA DE IMAGEN (IDÉNTICA A LA DE ADMIN) ---
    private void actualizarFotoSeleccionada() {
        int fila = tablaEventos.getSelectedRow();
        if (fila == -1) return;
        
        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        Evento evento = catalogo.buscarEvento(codigo);
        String ruta = evento.getRutaImagen();
        
        ImageIcon icono = null;
        try {
            if (ruta != null && (ruta.startsWith("http://") || ruta.startsWith("https://"))) {
                icono = new ImageIcon(new java.net.URL(ruta));
            } else if (ruta != null && !ruta.isEmpty()) {
                icono = new ImageIcon(ruta);
                if (icono.getImageLoadStatus() != MediaTracker.COMPLETE) {
                     java.net.URL resourceUrl = getClass().getResource(ruta);
                     if (resourceUrl != null) icono = new ImageIcon(resourceUrl);
                }
            }
        } catch (Exception e) {
            System.err.println("Error img: " + e.getMessage());
        }

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

    private void comprarEntrada() {
        int fila = tablaEventos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un evento.");
            return;
        }
        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        try {
            Evento evento = catalogo.buscarEvento(codigo);
            String tipo = (String) comboTipoEntrada.getSelectedItem();
            String metodo = (String) comboMetodoPago.getSelectedItem();
            int cantidad = (int) spinnerCantidad.getValue();
            Entrada entrada = FabricaEntradas.crearEntrada(tipo, evento);
            
            // PASARELA DE PAGO
            if ("Tarjeta".equals(metodo)) {
                double total = entrada.getPrecio() * cantidad;
                VentanaPasarelaPago pasarela = new VentanaPasarelaPago(this, total);
                pasarela.setVisible(true);
                if (!pasarela.isPagoRealizado()) {
                    JOptionPane.showMessageDialog(this, "Pago cancelado.");
                    return;
                }
            }

            ContextoPago pago = new ContextoPago();
            switch (metodo) {
                case "Tarjeta" -> pago.setEstrategia(new PagoTarjeta());
                case "Transferencia" -> pago.setEstrategia(new PagoTransferencia());
                case "PayPal" -> pago.setEstrategia(new PagoPayPal());
            }

            new GestorEntradas().comprar(evento, cliente, cantidad, entrada, pago);
            JOptionPane.showMessageDialog(this, "¡Entrada comprada con éxito!");
            cargarEventos(catalogo.listarEventos());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void verHorariosFestival() {
        int fila = tablaEventos.getSelectedRow();
        if (fila == -1) return;
        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        Evento e = catalogo.buscarEvento(codigo);
        if (e instanceof Festival f) new VentanaSubeventos(f).setVisible(true);
        else JOptionPane.showMessageDialog(this, "No es un festival.");
    }
}
