package vista;

import modelo.eventos.ComponenteEvento;
import modelo.eventos.Festival;
import modelo.eventos.Evento;
import controll.CatalogoEventos;

import javax.swing.*;
import java.awt.*;

public class VentanaEditarSubeventosFestival extends JFrame {

    private final Festival festival;
    private final CatalogoEventos catalogo;
    private final VentanaAdministrador padre;

    private DefaultListModel<String> modeloLista;
    private JList<String> lista;

    public VentanaEditarSubeventosFestival(Festival festival, CatalogoEventos catalogo, VentanaAdministrador padre) {
        this.festival = festival;
        this.catalogo = catalogo;
        this.padre = padre;

        setTitle("Editar subeventos de " + festival.getNombre());
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Estilos.aplicarEstiloVentana(this);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(Estilos.crearTitulo("Subeventos del Festival"), BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        lista = new JList<>(modeloLista);
        lista.setFont(Estilos.FONT_NORMAL);

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createTitledBorder("Agenda del Festival"));
        add(scroll, BorderLayout.CENTER);

        cargarSubeventos();

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(Estilos.COLOR_FONDO);

        JButton btnAñadir = Estilos.crearBoton("➕ Añadir Actividad", Estilos.COLOR_SECUNDARIO);
        JButton btnEliminar = Estilos.crearBoton("🗑 Eliminar Actividad", Color.RED);
        JButton btnGuardar = Estilos.crearBoton("💾 Guardar Cambios", Estilos.COLOR_PRIMARIO);

        btnAñadir.addActionListener(e -> new VentanaCrearSubevento(this).setVisible(true));

        btnEliminar.addActionListener(e -> eliminarSubevento());

        btnGuardar.addActionListener(e -> {
            catalogo.modificarEvento(festival, festival); // ya está modificado en memoria
            padre.cargarEventos();
            JOptionPane.showMessageDialog(this, "Subeventos actualizados correctamente.");
            dispose();
        });

        panelBotones.add(btnAñadir);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnGuardar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarSubeventos() {
        modeloLista.clear();
        for (ComponenteEvento c : festival.getSubeventos()) {
            modeloLista.addElement(c.toString());
        }
    }

    public void agregarSubevento(ComponenteEvento sub) {
        festival.agregarSubevento(sub);
        modeloLista.addElement(sub.toString());
    }

    private void eliminarSubevento() {
        int index = lista.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un subevento para eliminar.");
            return;
        }

        ComponenteEvento sub = festival.getSubeventos().get(index);
        festival.eliminarSubevento(sub);
        modeloLista.remove(index);
    }
}
