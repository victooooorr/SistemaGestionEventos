package vista;


import control.command.Invocador;
import control.command.ReservarEntrada;
import controll.CatalogoEventos;
import controll.GestorEntradas;
import controll.GestorVentas;
import excepciones.AforoCompletoException;
import excepciones.EventoNoEncontradoException;
import modelo.entradas.*;
import modelo.eventos.Evento;
import modelo.pagos.ContextoPago;
import modelo.pagos.PagoPayPal;
import modelo.pagos.PagoTarjeta;
import modelo.pagos.PagoTransferencia;
import modelo.usuarios.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import modelo.eventos.Festival;

public class VentanaCliente extends JFrame {

    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;
    private JButton comprarButton, refrescarButton;
    private JComboBox<String> metodoPagoCombo;
    private JComboBox<String> tipoEntradaCombo;

    private final Cliente cliente;
    private final CatalogoEventos catalogo;

    public VentanaCliente(Cliente cliente) {
        this.cliente = cliente;
        this.catalogo = CatalogoEventos.getInstancia();

        setTitle("Panel de Cliente - Bienvenido " + cliente.getNombre());
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   
        setJMenuBar(MenuSuperior.crearMenu(this, cliente));
        initComponents();
        cargarEventos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(
                new String[]{"Código", "Nombre", "Fecha", "Lugar", "Precio", "Aforo disponible"},
                0
        );

        tablaEventos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaEventos);

        comprarButton = new JButton("Comprar entrada");
        comprarButton.addActionListener(e -> comprarEntrada());

        refrescarButton = new JButton("Refrescar");
        refrescarButton.addActionListener(e -> cargarEventos());

        metodoPagoCombo = new JComboBox<>(new String[]{
                "Tarjeta",
                "PayPal",
                "Transferencia"
        });

        tipoEntradaCombo = new JComboBox<>(new String[]{
                "Básica",
                "Con Consumición",
                "VIP"
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(comprarButton);
        panelBotones.add(refrescarButton);

        panelBotones.add(new JLabel("Método de pago:"));
        panelBotones.add(metodoPagoCombo);

        panelBotones.add(new JLabel("Tipo de entrada:"));
        panelBotones.add(tipoEntradaCombo);

        JButton verTickets = new JButton("Mis Tickets");
        verTickets.addActionListener(e -> new VentanaTickets(cliente).setVisible(true));
        panelBotones.add(verTickets);

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        JButton verHorarios = new JButton("Ver horarios");
        verHorarios.addActionListener(e -> verHorariosFestival());
        panelBotones.add(verHorarios);

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


    private void cargarEventos() {
        modeloTabla.setRowCount(0);

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

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Confirmas la compra de " + cantidad + " entradas para " + evento.getNombre() + "?",
                    "Confirmar compra",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            ContextoPago pago = new ContextoPago();
            String metodo = (String) metodoPagoCombo.getSelectedItem();

            switch (metodo) {
                case "Tarjeta" -> pago.setEstrategia(new PagoTarjeta());
                case "PayPal" -> pago.setEstrategia(new PagoPayPal());
                case "Transferencia" -> pago.setEstrategia(new PagoTransferencia());
            }

            Entrada entrada = new EntradaBasica(evento);

            String tipo = (String) tipoEntradaCombo.getSelectedItem();

            switch (tipo) {
                case "Con Consumición" -> entrada = new EntradaConConsumicion(entrada);
                case "VIP" -> entrada = new EntradaVIP(entrada);
            }

            GestorEntradas gestor = new GestorEntradas();
            ReservarEntrada comando = new ReservarEntrada(gestor, evento, cliente, cantidad, entrada, pago);

            Invocador invocador = new Invocador();
            invocador.añadir(comando);
            invocador.ejecutarTodos();

            File ticket = TicketDigital.generarTicket(cliente, evento, cantidad, metodo, entrada);

            GestorVentas.getInstancia().registrarVenta(cliente, evento, cantidad, metodo);

            JOptionPane.showMessageDialog(this, "Compra realizada. Ticket generado:\n" + ticket.getAbsolutePath());

            cargarEventos();

        } catch (AforoCompletoException | EventoNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al comprar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
