package vista;

import control.observer.Observador;
import controll.CatalogoEventos;
import controll.GestorEntradas;

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

public class VentanaCliente extends JFrame implements Observador {

    private final Cliente cliente;
    private final CatalogoEventos catalogo;
    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;

    // ✅ Nuevos componentes para evitar castings inseguros
    private JComboBox<String> comboMetodoPago;
    private JComboBox<String> comboTipoEntrada;
    private JSpinner spinnerCantidad;

    public VentanaCliente(Cliente cliente) {
        this.cliente = cliente;
        this.catalogo = CatalogoEventos.getInstancia();

        setTitle("Panel de Cliente - Bienvenido " + cliente.getNombre());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    @Override
    public void actualizar(Evento e) {
        cargarEventos();
    }

    private void initComponents() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Fecha", "Lugar", "Precio", "Aforo disponible"}, 0
        );

        tablaEventos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaEventos);

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

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.EAST);

        refrescar.addActionListener(e -> cargarEventos());
        verHorarios.addActionListener(e -> verHorariosFestival());
        comprar.addActionListener(e -> comprarEntrada());
        misTickets.addActionListener(e -> verMisTickets());


        cargarEventos();
    }

    private void cargarEventos() {
        modeloTabla.setRowCount(0);

        Collection<Evento> eventos = catalogo.listarEventos();

        for (Evento e : eventos) {
            e.agregarObservador(this);

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

            Entrada entrada = new EntradaBasica(evento);

            if (tipo.equals("VIP")) {
                entrada = new EntradaVIP(entrada);
            } else if (tipo.equals("Premium")) {
                entrada = new EntradaConConsumicion(entrada);
            }

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





