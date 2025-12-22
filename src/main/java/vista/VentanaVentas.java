package vista;

import controll.ProxyVentas;
import controll.Venta;
import modelo.usuarios.Administrador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaVentas extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    public VentanaVentas(Administrador admin) {

        setTitle("Registro de Ventas");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents(admin);
    }

    private void initComponents(Administrador admin) {

        modelo = new DefaultTableModel(
                new String[]{"Fecha", "Cliente", "Evento", "Cantidad", "Método de pago"},
                0
        );

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);

        cargarVentas(admin);
    }

    private void cargarVentas(Administrador admin) {

        ProxyVentas proxy = new ProxyVentas(admin);
        List<Venta> ventas = proxy.obtenerVentas();

        modelo.setRowCount(0);

        for (Venta v : ventas) {
            modelo.addRow(new Object[]{
                    v.getFecha(),
                    v.getCliente().getNombre(),
                    v.getEvento().getNombre(),
                    v.getCantidad(),
                    v.getMetodoPago()
            });
        }
    }
}
