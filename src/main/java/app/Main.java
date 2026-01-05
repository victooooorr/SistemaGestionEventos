package app;

import controll.CatalogoEventos;
import controll.GestorVentas;
import controll.GestorUsuarios;
import modelo.eventos.*;
import modelo.usuarios.Administrador;
import modelo.usuarios.Cliente;
import vista.VentanaLogin;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        // 🛠️ CORRECCIÓN VISUAL PARA MAC Y WINDOWS
        try {
            // En lugar de Nimbus (que falla en Mac), usamos el diseño nativo del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo cargar el diseño del sistema.");
        }

        // ---------------------------------------------------------
        // 2. INICIALIZAR SISTEMAS
        // ---------------------------------------------------------
        GestorUsuarios gestorUsuarios = GestorUsuarios.getInstancia();
        CatalogoEventos catalogo = CatalogoEventos.getInstancia();

        // ---------------------------------------------------------
        // 3. DATOS DE PRUEBA
        // ---------------------------------------------------------
        
        Administrador admin = new Administrador("99999999X", "Super Admin", "admin@test.com", "admin", "600000001");
        gestorUsuarios.altaUsuario(admin, "admin");

        Cliente cliente1 = new Cliente("12345678A", "Ana López", "ana@test.com", "1234", "600000000", LocalDate.of(1995, 5, 20));
        gestorUsuarios.altaUsuario(cliente1, "1234");

        Cliente cliente2 = new Cliente("87654321B", "Carlos Pérez", "carlos@test.com", "abcd", "600000002", LocalDate.of(1990, 3, 10));
        gestorUsuarios.altaUsuario(cliente2, "abcd");

        // ---------------------------------------------------------
        // 4. EVENTOS DE PRUEBA
        // ---------------------------------------------------------
        
        Evento concierto = new Concierto("EV001", "Rock Night", LocalDateTime.now().plusDays(10), "Madrid Arena", 100, 25.0, "https://rock.com", "Rock", "The Flames", 120);
        concierto.setRutaImagen("imagenes/default.png"); 
        
        Evento teatro = new Teatro("EV002", "Hamlet", LocalDateTime.now().plusDays(5), "Teatro Real", 80, 30.0, "https://teatro.com", "Cía Nacional", 150);
        Evento conferencia = new Conferencia("EV003", "IA en 2026", LocalDateTime.now().plusDays(20), "IFEMA", 200, 15.0, "https://ia.com", "Dra. García", "IA", 90);

        Festival festival = new Festival("EV004", "Summer Fest", LocalDateTime.now().plusDays(30), "Parque Sol", 300, 40.0, "https://fest.com");
        festival.agregarSubevento(new HorarioFestival("Día 1", "18:00 - 02:00"));
        festival.agregarSubevento(new HorarioFestival("Día 2", "17:00 - 03:00"));

        catalogo.agregarEvento(concierto);
        catalogo.agregarEvento(teatro);
        catalogo.agregarEvento(conferencia);
        catalogo.agregarEvento(festival);

        // 🔥 RECUPERAR VENTAS ANTIGUAS
        System.out.println("🔄 Cargando historial de ventas...");
        GestorVentas.getInstancia().cargarVentasDesdeDisco();
        
        // ---------------------------------------------------------
        // 6. ARRANCAR
        // ---------------------------------------------------------
        SwingUtilities.invokeLater(() -> {
            new VentanaLogin(gestorUsuarios).setVisible(true);
        });
    }
}
