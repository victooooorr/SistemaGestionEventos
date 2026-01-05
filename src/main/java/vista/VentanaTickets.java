package vista;

import modelo.usuarios.Cliente;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class VentanaTickets extends JFrame {

    private final Cliente cliente;
    private JList<File> listaTickets;
    private DefaultListModel<File> modeloLista;

    public VentanaTickets(Cliente cliente) {
        this.cliente = cliente;
        setTitle("Mis Tickets - " + cliente.getNombre());
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Estilos.aplicarEstiloVentana(this);

        initComponents();
        cargarTickets();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(Estilos.crearTitulo("Mis Entradas Compradas"), BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        listaTickets = new JList<>(modeloLista);
        listaTickets.setFont(Estilos.FONT_NORMAL);
        listaTickets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTickets.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JScrollPane scroll = new JScrollPane(listaTickets);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        botones.setBackground(Estilos.COLOR_FONDO);
        botones.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton abrir = Estilos.crearBoton("📄 Abrir PDF/Ticket", Estilos.COLOR_PRIMARIO);
        JButton eliminar = Estilos.crearBoton("🗑️ Eliminar Historial", Estilos.COLOR_PELIGRO);

        abrir.addActionListener(e -> abrirTicket());
        eliminar.addActionListener(e -> eliminarTicket());

        botones.add(abrir);
        botones.add(eliminar);
        add(botones, BorderLayout.SOUTH);
    }

    private void cargarTickets() {
        modeloLista.clear();
        File carpeta = new File("tickets");
        if (!carpeta.exists()) return;

        File[] archivos = carpeta.listFiles((dir, name) -> name.startsWith("ticket_" + cliente.getDni()));
        if (archivos != null) {
            for (File f : archivos) modeloLista.addElement(f);
        }
    }

    private void abrirTicket() {
        File seleccionado = listaTickets.getSelectedValue();
        if (seleccionado == null) return;
        try {
            Desktop.getDesktop().open(seleccionado);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el ticket.");
        }
    }

    private void eliminarTicket() {
        File seleccionado = listaTickets.getSelectedValue();
        if (seleccionado == null) return;
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este ticket?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            seleccionado.delete();
            cargarTickets();
        }
    }
}
