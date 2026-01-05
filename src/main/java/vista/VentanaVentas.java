package vista;

import controll.ProxyVentas;
import modelo.ventas.Venta;
import modelo.eventos.Evento;
import modelo.usuarios.Administrador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class VentanaVentas extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private Evento eventoFiltro; // Puede ser null

    // Constructor 1: Ver TODAS las ventas
    public VentanaVentas(Administrador admin) {
        this(admin, null);
    }

    // Constructor 2: Ver ventas de UN EVENTO ESPECÍFICO
    public VentanaVentas(Administrador admin, Evento eventoFiltro) {
        this.eventoFiltro = eventoFiltro;
        
        if (eventoFiltro != null) {
            setTitle("Ventas del evento: " + eventoFiltro.getNombre());
        } else {
            setTitle("Registro Global de Ventas");
        }
        
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Estilos.aplicarEstiloVentana(this);

        initComponents(admin);
    }

    private void initComponents(Administrador admin) {
        setLayout(new BorderLayout());
        
        String titulo = (eventoFiltro != null) ? 
            "Ventas: " + eventoFiltro.getNombre() : "Informe Financiero Global";
            
        add(Estilos.crearTitulo(titulo), BorderLayout.NORTH);

        // Columnas añadidas: Nombre Cliente y Correo Cliente
        modelo = new DefaultTableModel(
                new String[]{"Fecha", "Cliente", "Correo", "Evento", "Cant.", "Total (€)"}, 0
        ) {
             @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tabla = new JTable(modelo);
        Estilos.estilizarTabla(tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);
        
        cargarVentas(admin);
    }

    private void cargarVentas(Administrador admin) {
        ProxyVentas proxy = new ProxyVentas(admin);
        List<Venta> todasLasVentas = proxy.obtenerVentas();
        List<Venta> ventasFiltradas;

        // LÓGICA DE FILTRADO
        if (eventoFiltro != null) {
            ventasFiltradas = todasLasVentas.stream()
                .filter(v -> v.getEvento().getCodigo().equals(eventoFiltro.getCodigo()))
                .collect(Collectors.toList());
        } else {
            ventasFiltradas = todasLasVentas;
        }

        modelo.setRowCount(0);
        double totalRecaudado = 0;
        int totalEntradas = 0;

        for (Venta v : ventasFiltradas) {
            modelo.addRow(new Object[]{
                    v.getFecha().toLocalDate(), // Solo fecha para que ocupe menos
                    v.getCliente().getNombre(),
                    v.getCliente().getCorreo(), // Dato útil para el admin
                    v.getEvento().getNombre(),
                    v.getCantidad(),
                    String.format("%.2f €", v.getPrecioFinal())
            });
            totalRecaudado += v.getPrecioFinal();
            totalEntradas += v.getCantidad();
        }
        
        // FOOTER CON RESUMEN
        JPanel footer = new JPanel(new GridLayout(1, 2));
        footer.setBackground(Estilos.COLOR_FONDO);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblEntradas = new JLabel("Entradas vendidas: " + totalEntradas);
        lblEntradas.setFont(Estilos.FONT_BOLD);
        
        JLabel lblTotal = new JLabel("Recaudación: " + String.format("%.2f €", totalRecaudado), SwingConstants.RIGHT);
        lblTotal.setFont(Estilos.FONT_TITULO);
        lblTotal.setForeground(Estilos.COLOR_EXITO);
        
        footer.add(lblEntradas);
        footer.add(lblTotal);
        
        add(footer, BorderLayout.SOUTH);
    }
}
