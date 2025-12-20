package app;

import controll.CatalogoEventos;
import controll.GestorUsuarios;
import modelo.eventos.*;
import modelo.usuarios.Administrador;
import modelo.usuarios.Cliente;
import vista.VentanaLogin;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        // ✅ Usamos el Singleton
        GestorUsuarios gestorUsuarios = GestorUsuarios.getInstancia();
        CatalogoEventos catalogo = CatalogoEventos.getInstancia();

        // ✅ Crear administrador de prueba
        Administrador admin = new Administrador(
                "99999999X", "Admin", "admin", "clave", "600000001"
        );
        gestorUsuarios.altaUsuario(admin, "admin");

        // ✅ Crear clientes de prueba
        Cliente cliente1 = new Cliente(
                "12345678A", "Ana López", "ana", "clave", "600000000",
                LocalDate.of(1995, 5, 20)
        );
        gestorUsuarios.altaUsuario(cliente1, "1234");

        Cliente cliente2 = new Cliente(
                "87654321B", "Carlos Pérez", "carlos", "clave", "600000002",
                LocalDate.of(1990, 3, 10)
        );
        gestorUsuarios.altaUsuario(cliente2, "abcd");

        // ✅ Crear eventos predeterminados
        Evento concierto = new Concierto(
                "EV001", "Rock Night",
                LocalDateTime.now().plusDays(10),
                "Madrid Arena",
                100, 25.0,
                "https://rocknight.com/info",
                "Rock", "The Flames", 120
        );

        Evento teatro = new Teatro(
                "EV002", "Hamlet",
                LocalDateTime.now().plusDays(5),
                "Teatro Real",
                80, 30.0,
                "https://teatroreal.com/hamlet",
                "Compañía Nacional", 150
        );

        Evento conferencia = new Conferencia(
                "EV003", "IA en 2025",
                LocalDateTime.now().plusDays(20),
                "IFEMA Madrid",
                200, 15.0,
                "https://ifema.es/ia2025",
                "Dra. García", "Inteligencia Artificial", 90
        );

        Festival festival = new Festival(
                "EV004", "Summer Fest",
                LocalDateTime.now().plusDays(30),
                "Parque del Sol",
                300, 40.0,
                "https://summerfest.com"
        );
        festival.agregarHorario("Día 1", "18:00 - 02:00");
        festival.agregarHorario("Día 2", "17:00 - 03:00");

        // ✅ Añadir eventos al catálogo
        catalogo.agregarEvento(concierto);
        catalogo.agregarEvento(teatro);
        catalogo.agregarEvento(conferencia);
        catalogo.agregarEvento(festival);

        // ✅ Lanzar la ventana de login
        new VentanaLogin(gestorUsuarios).setVisible(true);
    }
}
