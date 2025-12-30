package vista;

import controll.CatalogoEventos;
import modelo.eventos.ComponenteEvento;
import modelo.eventos.Evento;
import modelo.eventos.builder.FestivalAdapterBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaSubeventosFestival extends JFrame {

    private final FestivalAdapterBuilder builder;
    private final CatalogoEventos catalogo;
    private final VentanaAdministrador padre;

    private DefaultListModel<String> modeloLista;
    private JList<String> listaSubeventos;

    private List<ComponenteEvento> subeventos = new ArrayList<>();

    public VentanaSubeventosFestival(FestivalAdapterBuilder builder, CatalogoEventos catalogo, VentanaAdministrador padre) {
        this.builder = builder;
        this.catalogo = catalogo;
        this.padre = padre;

        setTitle("Añadir subeventos al Festival");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        modeloLista = new DefaultListModel<>();
        listaSubeventos = new JList<>(modeloLista);

        JScrollPane scroll = new JScrollPane(listaSubeventos);
        scroll.setBorder(BorderFactory.createTitledBorder("Subeventos añadidos"));

        JButton btnAñadir = new JButton("Añadir subevento");
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        JButton btnGuardar = new JButton("Guardar Festival");

        btnAñadir.addActionListener(e -> abrirVentanaCrearSubevento());
        btnEliminar.addActionListener(e -> eliminarSubevento());
        btnGuardar.addActionListener(e -> guardarFestival());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAñadir);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnGuardar);

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void abrirVentanaCrearSubevento() {
        new VentanaCrearSubevento(this).setVisible(true);
    }

    public void agregarSubevento(ComponenteEvento sub) {
        subeventos.add(sub);
        modeloLista.addElement(sub.getNombre());
    }

    private void eliminarSubevento() {
        int index = listaSubeventos.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un subevento.");
            return;
        }

        subeventos.remove(index);
        modeloLista.remove(index);
    }

    private void guardarFestival() {
        try {
            builder.conSubeventos(subeventos);

            Evento festival = builder.build();

            catalogo.agregarEvento(festival);

            JOptionPane.showMessageDialog(this, "Festival creado correctamente.");

            padre.cargarEventos();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar festival: " + ex.getMessage());
        }
    }
}
