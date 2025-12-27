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
import modelo.entradas.EntradaBasica;
import modelo.entradas.EntradaVIP;
import modelo.entradas.EntradaConConsumicion;

import modelo.pagos.ContextoPago;
import modelo.pagos.PagoTarjeta;
import modelo.pagos.PagoTransferencia;
import modelo.pagos.PagoPayPal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;
import modelo.entradas.factory.FabricaEntradas;

public class VentanaCliente extends JFrame implements Observador {

    private final Cliente cliente;
    private final CatalogoEventos catalogo;
    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> comboMetodoPago;
    private JComboBox<String> comboTipoEntrada;
    private JSpinner spinnerCantidad;

    // 🟦 Panel de notificaciones
    private JTextArea areaNotificaciones;

    public VentanaCliente(Cliente cliente) {
        this.cliente = cliente;
        this.catalogo = CatalogoEventos.getInstancia();

        setTitle("Panel de Cliente - Bienvenido " + cliente.getNombre());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    // 🟦 OBSERVER: actualización estándar
    @Override
    public void actualizar(Evento e) {
        cargarEventos();
        areaNotificaciones.append(
                "[Actualización] " + e.getNombre() +
                " | Nuevo aforo: " + e.getAforoDisponible() + "\n"
        );
    }

    // 🟦 OBSERVER: mensajes personalizados
    @Override
    public void actualizarMensaje(String mensaje, Evento e) {
        areaNotificaciones.append("[Notificación] " + mensaje + "\n");
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        // ---------------- TABLA DE EVENTOS ----------------
        modeloTabla = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Fecha", "Lugar", "Precio", "Aforo disponible"}, 0
        );

        tablaEventos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaEventos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Eventos disponibles"));

        add(scrollTabla, BorderLayout.CENTER);

        // ---------------- PANEL LATERAL (COMPRA) ----------------
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 5, 5));

        JButton comprar = new JButton("Comprar entrada");
        JButton refrescar = new JButton("Refrescar");
        JButton misTickets = new JButton("Mis Tickets");
        JButton verHorarios = new JButton("Ver horarios");

        comboMetodoPago = new JComboBox<>(new String[]{"Tarjeta", "Transferencia", "PayPal"});
        comboTipoEntrada = new JComboBox<>(new String[]{"Básica", "VIP", "Premium"});
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        panelBotones.add(comprar);
        panelBotones.add(refrescar);
        panelBotones.add(misTickets);
        panelBotones.add(verHorarios);
        panelBotones.add(new JLabel("Método de pago:"));
        panelBotones.add(comboMetodoPago);
        panelBotones.add(new JLabel("Tipo de entrada:"));
        panelBotones.add(comboTipoEntrada);
        panelBotones.add(new JLabel("Cantidad:"));
        panelBotones.add(spinnerCantidad);

        add(panelBotones, BorderLayout.EAST);

        // ---------------- PANEL INFERIOR (BOTONES + NOTIFICACIONES) ----------------
        JPanel panelInferior = new JPanel(new BorderLayout());

        // Botones inferiores
        JPanel panelBotonesInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        JButton btnSalir = new JButton("Salir");

        panelBotonesInferior.add(btnCerrarSesion);
        panelBotonesInferior.add(btnSalir);

        // Panel de notificaciones
        areaNotificaciones = new JTextArea();
        areaNotificaciones.setEditable(false);
        areaNotificaciones.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollNotificaciones = new JScrollPane(areaNotificaciones);
        scrollNotificaciones.setPreferredSize(new Dimension(900, 120));
        scrollNotificaciones.setBorder(BorderFactory.createTitledBorder("Notificaciones"));
        
        for (String n : GestorNotificaciones.obtener()) {
    areaNotificaciones.append(n + "\n");
}

        
        panelInferior.add(panelBotonesInferior, BorderLayout.NORTH);
        panelInferior.add(scrollNotificaciones, BorderLayout.CENTER);

        add(panelInferior, BorderLayout.SOUTH);

        // ---------------- LISTENERS ----------------
        refrescar.addActionListener(e -> cargarEventos());
        verHorarios.addActionListener(e -> verHorariosFestival());
        comprar.addActionListener(e -> comprarEntrada());
        misTickets.addActionListener(e -> verMisTickets());

        btnCerrarSesion.addActionListener(e -> {
    dispose();
    new VentanaLogin(GestorUsuarios.getInstancia()).setVisible(true);
});


        btnSalir.addActionListener(e -> System.exit(0));

        cargarEventos();
    }

    private void cargarEventos() {
        modeloTabla.setRowCount(0);

        Collection<Evento> eventos = catalogo.listarEventos();

        for (Evento e : eventos) {
           if (!e.getObservadores().contains(this)) { 
               e.agregarObservador(this); }

            modeloTabla.addRow(new Object[]{
                    e.getCodigo(),
                    e.getNombre(),
                    e.getFechaHora(),
                    e.getLugar(),
                    e.getPrecioBase(),
                    e.getAforoDisponible()
            });
        }
    }

    private void comprarEntrada() {
        int fila = tablaEventos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un evento primero.");
            return;
        }

        String codigo = (String) modeloTabla.getValueAt(fila, 0);

        try {
            Evento evento = catalogo.buscarEvento(codigo);

            String tipo = (String) comboTipoEntrada.getSelectedItem();
            String metodo = (String) comboMetodoPago.getSelectedItem();
            int cantidad = (int) spinnerCantidad.getValue();

           Entrada entrada = FabricaEntradas.crearEntrada(tipo, evento);

            ContextoPago pago = new ContextoPago();

            switch (metodo) {
                case "Tarjeta" -> pago.setEstrategia(new PagoTarjeta());
                case "Transferencia" -> pago.setEstrategia(new PagoTransferencia());
                case "PayPal" -> pago.setEstrategia(new PagoPayPal());
            }

            new GestorEntradas().comprar(evento, cliente, cantidad, entrada, pago);

            JOptionPane.showMessageDialog(this, "Entrada comprada correctamente.");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void verHorariosFestival() {
        int fila = tablaEventos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un evento primero.");
            return;
        }

        String codigo = (String) modeloTabla.getValueAt(fila, 0);

        try {
            Evento evento = catalogo.buscarEvento(codigo);

            if (evento instanceof Festival festival) {
                new VentanaSubeventos(festival).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Este evento no es un festival.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void verMisTickets() {
        new VentanaTickets(cliente).setVisible(true);
    }
}





