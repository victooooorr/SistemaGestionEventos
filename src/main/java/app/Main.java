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

        // CORRECCIÓN VISUAL PARA MAC Y WINDOWS
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
        concierto.setRutaImagen("/imagenes/rock_nights.png"); 
        
        Evento teatro = new Teatro("EV002", "Hamlet", LocalDateTime.now().plusDays(5), "Teatro Real", 80, 30.0, "https://teatro.com", "Cía Nacional", 150);
        teatro.setRutaImagen("/imagenes/hamlet.jpg");
        Evento conferencia = new Conferencia("EV003", "IA en 2026", LocalDateTime.now().plusDays(20), "IFEMA", 200, 15.0, "https://ia.com", "Dra. García", "IA", 90);
        conferencia.setRutaImagen("/imagenes/evento_ia.png");

        Festival festival = new Festival("EV004", "Summer Fest", LocalDateTime.now().plusDays(30), "Parque Sol", 300, 40.0, "https://fest.com");
        festival.agregarSubevento(new HorarioFestival("Día 1", "18:00 - 02:00"));
        festival.agregarSubevento(new HorarioFestival("Día 2", "17:00 - 03:00"));
        festival.setRutaImagen("/imagenes/summer_fest.png");

        catalogo.agregarEvento(concierto);
        catalogo.agregarEvento(teatro);
        catalogo.agregarEvento(conferencia);
        catalogo.agregarEvento(festival);
        // ---------------------------------------------------------
        // EVENTOS EXTRA GENERADOS AUTOMÁTICAMENTE
        // ---------------------------------------------------------

        // 5. Concierto
        Evento ev005 = new Concierto("EV005", "Jazz & Blues Night",
                LocalDateTime.now().plusDays(12), "Sala Clamores", 120, 22.0,
                "https://jazz.com", "Jazz", "Blue Spirits", 110);
        ev005.setRutaImagen("/imagenes/jazz_blues.png");
        catalogo.agregarEvento(ev005);

        // 6. Teatro
        Evento ev006 = new Teatro("EV006", "La Casa de Bernarda Alba",
                LocalDateTime.now().plusDays(18), "Teatro Español", 90, 28.0,
                "https://teatroespanol.com", "Compañía Dramática Madrid", 140);
        ev006.setRutaImagen("/imagenes/bernarda_alba.jpg");
        catalogo.agregarEvento(ev006);

        // 7. Conferencia
        Evento ev007 = new Conferencia("EV007", "Ciberseguridad 2030",
                LocalDateTime.now().plusDays(25), "IFEMA Madrid", 250, 18.0,
                "https://ciberseguridad.com", "Ing. Torres", "Seguridad", 100);
        ev007.setRutaImagen("/imagenes/ciberseguridad.png");
        catalogo.agregarEvento(ev007);

        // 8. Concierto
        Evento ev008 = new Concierto("EV008", "Pop Stars Live",
                LocalDateTime.now().plusDays(40), "Wizink Center", 300, 35.0,
                "https://popstars.com", "Pop", "Luna Vega", 130);
        ev008.setRutaImagen("/imagenes/popstars.png");
        catalogo.agregarEvento(ev008);

        // 9. Conferencia
        Evento ev009 = new Conferencia("EV009", "Blockchain Summit",
                LocalDateTime.now().plusDays(15), "Campus Google Madrid", 180, 20.0,
                "https://blockchain.com", "Dr. Salvatierra", "Tecnología", 80);
        ev009.setRutaImagen("/imagenes/blockchain.png");
        catalogo.agregarEvento(ev009);

        // 10. Teatro
        Evento ev010 = new Teatro("EV010", "El Lago de los Cisnes",
                LocalDateTime.now().plusDays(50), "Teatro Real", 150, 45.0,
                "https://ballet.com", "Ballet Nacional", 160);
        ev010.setRutaImagen("/imagenes/lago_cisnes.jpg");
        catalogo.agregarEvento(ev010);

        // 11. Concierto
        Evento ev011 = new Concierto("EV011", "Metal Storm",
                LocalDateTime.now().plusDays(22), "La Riviera", 200, 27.0,
                "https://metalstorm.com", "Metal", "Iron Wolves", 100);
        ev011.setRutaImagen("/imagenes/metal_storm.png");
        catalogo.agregarEvento(ev011);

        // 12. Conferencia
        Evento ev012 = new Conferencia("EV012", "Marketing Digital Pro",
                LocalDateTime.now().plusDays(33), "Auditorio BBVA", 160, 17.0,
                "https://marketingpro.com", "Laura Méndez", "Marketing", 90);
        ev012.setRutaImagen("/imagenes/marketing.png");
        catalogo.agregarEvento(ev012);

        // 13. Festival con subeventos
        Festival ev013 = new Festival("EV013", "TechFest Madrid",
                LocalDateTime.now().plusDays(60), "IFEMA Pabellón 5", 500, 55.0,
                "https://techfest.com");
        ev013.agregarSubevento(new HorarioFestival("Conferencia IA", "10:00 - 12:00"));
        ev013.agregarSubevento(new HorarioFestival("Taller Cloud", "12:30 - 14:00"));
        ev013.agregarSubevento(new HorarioFestival("Concierto Synthwave", "20:00 - 23:00"));
        ev013.setRutaImagen("/imagenes/techfest.png");
        catalogo.agregarEvento(ev013);

        // 14. Festival con subeventos
        Festival ev014 = new Festival("EV014", "GastroMusic Fest",
                LocalDateTime.now().plusDays(45), "Casa de Campo", 400, 30.0,
                "https://gastromusic.com");
        ev014.agregarSubevento(new HorarioFestival("Cocina Fusión", "13:00 - 15:00"));
        ev014.agregarSubevento(new HorarioFestival("Concierto Indie", "18:00 - 21:00"));
        ev014.setRutaImagen("/imagenes/gastromusic.png");
        catalogo.agregarEvento(ev014);

        // 15. Teatro
        Evento ev015 = new Teatro("EV015", "Don Juan Tenorio",
                LocalDateTime.now().plusDays(12), "Teatro Alcalá", 100, 26.0,
                "https://donjuan.com", "Compañía Clásicos", 120);
        ev015.setRutaImagen("/imagenes/don_juan.jpg");
        catalogo.agregarEvento(ev015);

        // 16. Concierto
        Evento ev016 = new Concierto("EV016", "ElectroWave Festival",
                LocalDateTime.now().plusDays(70), "IFEMA Explanada", 350, 32.0,
                "https://electrowave.com", "Electrónica", "DJ Nova", 140);
        ev016.setRutaImagen("/imagenes/electrowave.png");
        catalogo.agregarEvento(ev016);

        // 17. Conferencia
        Evento ev017 = new Conferencia("EV017", "Neurociencia Moderna",
                LocalDateTime.now().plusDays(28), "Universidad Autónoma", 120, 14.0,
                "https://neurociencia.com", "Dr. Ruiz", "Ciencia", 75);
        ev017.setRutaImagen("/imagenes/neurociencia.png");
        catalogo.agregarEvento(ev017);

        // 18. Festival con subeventos
        Festival ev018 = new Festival("EV018", "Anime Expo Madrid",
                LocalDateTime.now().plusDays(90), "IFEMA Pabellón 9", 600, 45.0,
                "https://animeexpo.com");
        ev018.agregarSubevento(new HorarioFestival("Concurso Cosplay", "11:00 - 13:00"));
        ev018.agregarSubevento(new HorarioFestival("K-Pop Dance", "16:00 - 18:00"));
        ev018.agregarSubevento(new HorarioFestival("Proyección Anime", "19:00 - 22:00"));
        ev018.setRutaImagen("/imagenes/anime_expo.png");
        catalogo.agregarEvento(ev018);

        // 19. Concierto
        Evento ev019 = new Concierto("EV019", "Acoustic Sessions",
                LocalDateTime.now().plusDays(8), "Café Berlín", 70, 18.0,
                "https://acoustic.com", "Acústico", "Sofía Ríos", 90);
        ev019.setRutaImagen("/imagenes/acoustic.png");
        catalogo.agregarEvento(ev019);


        // RECUPERAR VENTAS ANTIGUAS
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
