package vista;

import control.observer.Observador;
import controll.CatalogoEventos;

import modelo.eventos.Evento;
import modelo.eventos.Festival;
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

    public VentanaAdministrador(Administrador admin) {
        this.admin = admin;
        this.catalogo = CatalogoEventos.getInstancia();

        setTitle("Panel de Administrador - " + admin.getNombre());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

        JButton refrescar = new JButton("Refrescar");
        JButton eliminar = new JButton("Eliminar evento");
        JButton modificar = new JButton("Modificar evento");

        panelBotones.add(refrescar);
        panelBotones.add(eliminar);
        panelBotones.add(modificar);

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.EAST);

        refrescar.addActionListener(e -> cargarEventos());
        eliminar.addActionListener(e -> eliminarEvento());
        modificar.addActionListener(e -> modificarEvento());

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


