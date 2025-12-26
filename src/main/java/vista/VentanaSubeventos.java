package vista;

import modelo.eventos.ComponenteEvento;
import modelo.eventos.Festival;

import javax.swing.*;
import java.awt.*;

public class VentanaSubeventos extends JFrame {

    private final Festival festival;
    private JList<String> lista;
    private DefaultListModel<String> modelo;

    public VentanaSubeventos(Festival festival) {
        this.festival = festival;

        setTitle("Horarios de " + festival.getNombre());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        modelo = new DefaultListModel<>();
        lista = new JList<>(modelo);

        JScrollPane scroll = new JScrollPane(lista);

        add(scroll, BorderLayout.CENTER);

        cargarSubeventos();
    }

    private void cargarSubeventos() {
        modelo.clear();

        for (ComponenteEvento c : festival.getSubeventos()) {
        modelo.addElement(c.toString());
        }

    }
}
