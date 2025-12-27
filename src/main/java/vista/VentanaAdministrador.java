package vista;

import control.observer.Observador;
import controll.CatalogoEventos;
import controll.GestorNotificaciones;
import controll.GestorUsuarios;

import modelo.eventos.Evento;
import modelo.usuarios.Administrador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class VentanaAdministrador extends JFrame implements Observador {

    private final CatalogoEventos catalogo;
    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;
    private final Administrador admin;

    // 🟦 Panel de notificaciones
    private JTextArea areaNotificaciones;

    public VentanaAdministrador(Administrador admin) {
        this.admin = admin;
        this.catalogo = CatalogoEventos.getInstancia();

        setTitle("Panel de Administrador - " + admin.getNombre());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    // 🟦 OBSERVER: actualización estándar
    @Override
    public void actualizar(Evento e) {
        cargarEventos();
        areaNotificaciones.append(
                "[Actualización] " + e.getNombre() +
                " | Nuevo aforo: " + e.getAforoDisponible() + "\n"
        );
    }

    // 🟦 OBSERVER: mensajes personalizados
    @Override
    public void actualizarMensaje(String mensaje, Evento e) {
        areaNotificaciones.append("[Notificación] " + mensaje + "\n");
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        // ---------------- TABLA DE EVENTOS ----------------
        modeloTabla = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Fecha", "Lugar", "Precio", "Aforo disponible"}, 0
        );

        tablaEventos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaEventos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Eventos registrados"));

        add(scrollTabla, BorderLayout.CENTER);

        // ---------------- PANEL LATERAL ----------------
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 5, 5));

        JButton refrescar = new JButton("Refrescar");
        JButton eliminar = new JButton("Eliminar evento");
        JButton modificar = new JButton("Modificar evento");

        panelBotones.add(refrescar);
        panelBotones.add(eliminar);
        panelBotones.add(modificar);

        add(panelBotones, BorderLayout.EAST);

        // ---------------- PANEL INFERIOR (BOTONES + NOTIFICACIONES) ----------------
        JPanel panelInferior = new JPanel(new BorderLayout());

        // Botones inferiores
        JPanel panelBotonesInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        JButton btnSalir = new JButton("Salir");

        panelBotonesInferior.add(btnCerrarSesion);
        panelBotonesInferior.add(btnSalir);

        // Panel de notificaciones
        areaNotificaciones = new JTextArea();
        areaNotificaciones.setEditable(false);
        areaNotificaciones.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollNotificaciones = new JScrollPane(areaNotificaciones);
        scrollNotificaciones.setPreferredSize(new Dimension(900, 150));
        scrollNotificaciones.setBorder(BorderFactory.createTitledBorder("Notificaciones del sistema"));

        for (String n : GestorNotificaciones.obtener()) {
    areaNotificaciones.append(n + "\n");
}

        
        panelInferior.add(panelBotonesInferior, BorderLayout.NORTH);
        panelInferior.add(scrollNotificaciones, BorderLayout.CENTER);

        add(panelInferior, BorderLayout.SOUTH);

        // ---------------- LISTENERS ----------------
        refrescar.addActionListener(e -> cargarEventos());
        eliminar.addActionListener(e -> eliminarEvento());
        modificar.addActionListener(e -> modificarEvento());

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new VentanaLogin(GestorUsuarios.getInstancia()).setVisible(true);
        });

        btnSalir.addActionListener(e -> System.exit(0));

        cargarEventos();
    }

    private void cargarEventos() {
        modeloTabla.setRowCount(0);

        Collection<Evento> eventos = catalogo.listarEventos();

        for (Evento e : eventos) {

            // Evitar registrar varias veces al administrador
            if (!e.getObservadores().contains(this)) {
                e.agregarObservador(this);
            }

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

    private void eliminarEvento() {
        int fila = tablaEventos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un evento primero.");
            return;
        }

        String codigo = (String) modeloTabla.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar el evento " + codigo + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            catalogo.eliminarEvento(codigo);
            cargarEventos();
        }
    }

    private void modificarEvento() {
        JOptionPane.showMessageDialog(this,
                "La función de modificar evento aún no está implementada.");
    }
}



