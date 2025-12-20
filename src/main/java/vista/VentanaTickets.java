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
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        cargarTickets();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        modeloLista = new DefaultListModel<>();
        listaTickets = new JList<>(modeloLista);

        JScrollPane scroll = new JScrollPane(listaTickets);

        JButton abrir = new JButton("Abrir ticket");
        abrir.addActionListener(e -> abrirTicket());

        JButton eliminar = new JButton("Eliminar ticket");
        eliminar.addActionListener(e -> eliminarTicket());

        JPanel botones = new JPanel();
        botones.add(abrir);
        botones.add(eliminar);

        add(scroll, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void cargarTickets() {
        modeloLista.clear();

        File carpeta = new File("tickets");
        if (!carpeta.exists()) return;

        File[] archivos = carpeta.listFiles((dir, name) ->
                name.startsWith("ticket_" + cliente.getDni())
        );

        if (archivos != null) {
            for (File f : archivos) modeloLista.addElement(f);
        }
    }

    private void abrirTicket() {
        File seleccionado = listaTickets.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un ticket.");
            return;
        }

        try {
            Desktop.getDesktop().open(seleccionado);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el ticket.");
        }
    }

    private void eliminarTicket() {
        File seleccionado = listaTickets.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un ticket.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar este ticket?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            seleccionado.delete();
            cargarTickets();
        }
    }
}
