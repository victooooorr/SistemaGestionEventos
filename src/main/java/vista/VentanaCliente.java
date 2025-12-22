package vista;

import controll.CatalogoEventos;
import modelo.eventos.Evento;
import modelo.eventos.Festival;
import modelo.eventos.ComponenteEvento;
import modelo.eventos.HorarioFestival;
import modelo.usuarios.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;
import java.util.List;

public class VentanaCliente extends JFrame {

    private final Cliente cliente;
    private final CatalogoEventos catalogo;
    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;

    public VentanaCliente(Cliente cliente) {
        this.cliente = cliente;
        this.catalogo = CatalogoEventos.getInstancia();

        setTitle("Panel de Cliente - Bienvenido " + cliente.getNombre());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        modeloTabla = new DefaultTableModel(new Object[]{"Código", "Nombre", "Fecha", "Lugar", "Precio", "Aforo disponible"}, 0);
        tablaEventos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaEventos);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 5, 5));

        JButton comprar = new JButton("Comprar entrada");
        JButton refrescar = new JButton("Refrescar");
        JButton misTickets = new JButton("Mis Tickets");
        JButton verHorarios = new JButton("Ver horarios");

        JComboBox<String> metodoPago = new JComboBox<>(new String[]{"Tarjeta", "Bizum", "Transferencia"});
        JComboBox<String> tipoEntrada = new JComboBox<>(new String[]{"Básica", "VIP", "Premium"});

        panelBotones.add(comprar);
        panelBotones.add(refrescar);
        panelBotones.add(misTickets);
        panelBotones.add(verHorarios);
        panelBotones.add(new JLabel("Método de pago:"));
        panelBotones.add(metodoPago);
        panelBotones.add(new JLabel("Tipo de entrada:"));
        panelBotones.add(tipoEntrada);

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.EAST);

        refrescar.addActionListener(e -> cargarEventos());
        verHorarios.addActionListener(e -> verHorariosFestival());

        cargarEventos();
    }

    private void cargarEventos() {
        modeloTabla.setRowCount(0);

        Collection<Evento> eventos = catalogo.listarEventos();

        for (Evento e : eventos) {
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
}
