package vista;

import controll.CatalogoEventos;
import controll.GestorEntradas;
import control.command.Invocador;
import control.command.ReservarEntrada;
import controll.GestorVentas;
import excepciones.AforoCompletoException;
import excepciones.EventoNoEncontradoException;
import modelo.entradas.EntradaBasica;
import modelo.eventos.Evento;
import modelo.pagos.ContextoPago;
import modelo.pagos.PagoTarjeta;
import modelo.usuarios.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import modelo.entradas.TicketDigital;
import modelo.pagos.PagoPayPal;
import modelo.pagos.PagoTransferencia;

public class VentanaCliente extends JFrame {

    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;
    private JButton comprarButton, refrescarButton;
    private JTextArea areaNotificaciones;
    private JButton verNotificacionesButton;
    private JComboBox<String> metodoPagoCombo;



    private final Cliente cliente;
    private final CatalogoEventos catalogo;

    public VentanaCliente(Cliente cliente) {
        this.cliente = cliente;
        this.catalogo = CatalogoEventos.getInstancia();

        setTitle("Panel de Cliente - Bienvenido " + cliente.getNombre());
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setJMenuBar(MenuSuperior.crearMenu(this, cliente));


        initComponents();
        cargarEventos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // ✅ Tabla de eventos
        modeloTabla = new DefaultTableModel(
                new String[]{"Código", "Nombre", "Fecha", "Lugar", "Precio", "Aforo disponible"},
                0
        );

        tablaEventos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaEventos);

        // ✅ Botones
        comprarButton = new JButton("Comprar entrada");
        comprarButton.addActionListener(e -> comprarEntrada());

        refrescarButton = new JButton("Refrescar");
        refrescarButton.addActionListener(e -> cargarEventos());

        JPanel panelBotones = new JPanel();
        panelBotones.add(comprarButton);
        panelBotones.add(refrescarButton);

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        areaNotificaciones = new JTextArea(5, 20);
        areaNotificaciones.setEditable(false);
        JScrollPane scrollNotificaciones = new JScrollPane(areaNotificaciones);

        verNotificacionesButton = new JButton("Ver notificaciones");
        verNotificacionesButton.addActionListener(e -> cargarNotificaciones());

        JPanel panelNotificaciones = new JPanel(new BorderLayout());
        panelNotificaciones.setBorder(BorderFactory.createTitledBorder("Notificaciones"));
        panelNotificaciones.add(scrollNotificaciones, BorderLayout.CENTER);
        panelNotificaciones.add(verNotificacionesButton, BorderLayout.SOUTH);

        add(panelNotificaciones, BorderLayout.EAST);
        metodoPagoCombo = new JComboBox<>(new String[]{
                "Tarjeta",
                "PayPal",
                "Transferencia"
        });

        panelBotones.add(new JLabel("Método de pago:"));
        panelBotones.add(metodoPagoCombo);
        
        JButton verTickets = new JButton("Mis Tickets");
        verTickets.addActionListener(e -> new VentanaTickets(cliente).setVisible(true));

        panelBotones.add(verTickets);
        
        




    }

    private void cargarEventos() {
        modeloTabla.setRowCount(0); // limpiar tabla

        List<Evento> eventos = catalogo.listarEventos().stream().toList();

        for (Evento e : eventos) {
            modeloTabla.addRow(new Object[]{
                    e.getCodigo(),
                    e.getNombre(),
                    e.getFechaHora().toString(),
                    e.getLugar(),
                    e.getPrecioBase(),
                    e.getAforoDisponible()
            });
        }
    }
    private void cargarNotificaciones() {
        areaNotificaciones.setText("");

        for (String n : cliente.getNotificaciones()) {
            areaNotificaciones.append("• " + n + "\n");
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

        String cantidadStr = JOptionPane.showInputDialog(this, "¿Cuántas entradas quieres?");
        if (cantidadStr == null) return;

        int cantidad = Integer.parseInt(cantidadStr);

        // ✅ Confirmación de compra
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Confirmas la compra de " + cantidad + " entradas para " + evento.getNombre() + "?",
                "Confirmar compra",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        // ✅ Configurar pago con Strategy
        ContextoPago pago = new ContextoPago();

        String metodo = (String) metodoPagoCombo.getSelectedItem();

        switch (metodo) {
            case "Tarjeta" -> pago.setEstrategia(new PagoTarjeta());
            case "PayPal" -> pago.setEstrategia(new PagoPayPal());
            case "Transferencia" -> pago.setEstrategia(new PagoTransferencia());
        }

        // ✅ Crear entrada básica
        EntradaBasica entrada = new EntradaBasica(evento);

        // ✅ Ejecutar compra usando Command
        GestorEntradas gestor = new GestorEntradas();
        ReservarEntrada comando = new ReservarEntrada(gestor, evento, cliente, cantidad, entrada, pago);

        Invocador invocador = new Invocador();
        invocador.añadir(comando);
        invocador.ejecutarTodos();

        // ✅ Generar ticket digital
        TicketDigital.generarTicket(cliente, evento, cantidad, metodo);

        // ✅ Registrar venta
        GestorVentas.getInstancia().registrarVenta(cliente, evento, cantidad, metodo);

        JOptionPane.showMessageDialog(this, "✅ Compra realizada. Ticket generado.");

        cargarEventos(); // refrescar aforo

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al comprar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

}
