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
    private List<ComponenteEvento> subeventos = new ArrayList<>();

    public VentanaSubeventosFestival(FestivalAdapterBuilder builder, CatalogoEventos catalogo, VentanaAdministrador padre) {
        this.builder = builder;
        this.catalogo = catalogo;
        this.padre = padre;

        setTitle("Configurar Festival");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Estilos.aplicarEstiloVentana(this);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(Estilos.crearTitulo("Añadir Subeventos al Festival"), BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        JList<String> listaSubeventos = new JList<>(modeloLista);
        listaSubeventos.setFont(Estilos.FONT_NORMAL);
        
        JScrollPane scroll = new JScrollPane(listaSubeventos);
        scroll.setBorder(BorderFactory.createTitledBorder("Agenda del Festival"));
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(Estilos.COLOR_FONDO);

        JButton btnAñadir = Estilos.crearBoton("➕ Añadir Actividad", Estilos.COLOR_SECUNDARIO);
        JButton btnGuardar = Estilos.crearBoton("💾 Finalizar Festival", Estilos.COLOR_PRIMARIO);

        btnAñadir.addActionListener(e -> new VentanaCrearSubevento(this).setVisible(true));
        
        btnGuardar.addActionListener(e -> {
            try {
                builder.conSubeventos(subeventos);
                Evento festival = builder.build();
                catalogo.agregarEvento(festival);
                JOptionPane.showMessageDialog(this, "Festival creado con éxito.");
                padre.cargarEventos();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        panelBotones.add(btnAñadir);
        panelBotones.add(btnGuardar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void agregarSubevento(ComponenteEvento sub) {
        subeventos.add(sub);
        modeloLista.addElement(sub.getNombre());
    }
}
