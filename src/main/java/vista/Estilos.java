package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Estilos {

    // --- COLORES ---
    public static final Color COLOR_FONDO = new Color(240, 242, 245);
    public static final Color COLOR_PRIMARIO = new Color(59, 89, 152);
    public static final Color COLOR_SECUNDARIO = new Color(100, 149, 237);
    public static final Color COLOR_TEXTO = new Color(50, 50, 50);
    public static final Color COLOR_BLANCO = Color.WHITE;
    public static final Color COLOR_EXITO = new Color(66, 183, 42);
    public static final Color COLOR_PELIGRO = new Color(220, 53, 69);
    public static final Color COLOR_NARANJA = Color.ORANGE;

    // --- FUENTES (USAMOS ARIAL + TAMAÑO 12 PARA QUE QUEPA TODO) ---
    public static final Font FONT_TITULO = new Font("Arial", Font.BOLD, 22);
    public static final Font FONT_SUBTITULO = new Font("Arial", Font.BOLD, 16);
    // Bajamos a 12px para asegurar que textos largos no se corten
    public static final Font FONT_BOLD = new Font("Arial", Font.BOLD, 12); 
    public static final Font FONT_NORMAL = new Font("Arial", Font.PLAIN, 12);
    public static final Font FONT_TEXTO = FONT_NORMAL;

    // --- FONDOS ---
    public static void aplicarEstiloVentana(JFrame frame) {
        frame.getContentPane().setBackground(COLOR_FONDO);
    }
    public static void aplicarEstiloVentana(JDialog dialog) {
        dialog.getContentPane().setBackground(COLOR_FONDO);
    }

    // --- BOTONES ---
    public static JButton crearBoton(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BOLD);
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        
        // Configuraciones para compatibilidad total
        btn.setOpaque(true); 
        btn.setBorderPainted(false); 
        btn.setFocusPainted(false);
        
        // Reducimos el padding lateral de 20 a 10 para ganar espacio para el texto
        btn.setBorder(new EmptyBorder(8, 10, 8, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }

    // Botones secundarios
    public static JButton crearBoton(String texto, boolean esPrincipal) {
        if (esPrincipal) return crearBoton(texto, COLOR_PRIMARIO);

        JButton btn = new JButton(texto);
        btn.setFont(FONT_NORMAL);
        btn.setBackground(Color.WHITE);
        btn.setForeground(COLOR_TEXTO);
        
        btn.setOpaque(true);
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Quitamos el tamaño fijo preferido para que se adapte al contenido
        // btn.setPreferredSize(new Dimension(150, 40)); 
        return btn;
    }
    
    // --- OTROS COMPONENTES ---
    public static JLabel crearTitulo(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(FONT_TITULO);
        lbl.setForeground(COLOR_PRIMARIO);
        lbl.setBorder(new EmptyBorder(20, 0, 20, 0));
        return lbl;
    }

    // --- TABLA ---
    public static void estilizarTabla(JTable tabla) {
        tabla.setRowHeight(30);
        tabla.setFont(FONT_NORMAL);
        tabla.setSelectionBackground(new Color(232, 240, 254));
        tabla.setSelectionForeground(Color.BLACK);

        JTableHeader header = tabla.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setReorderingAllowed(false);
    }

    static class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setBackground(COLOR_PRIMARIO);
            setForeground(Color.WHITE);
            setFont(FONT_BOLD);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText(value.toString());
            return this;
        }
    }
}
