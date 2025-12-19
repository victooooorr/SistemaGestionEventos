package vista;


import controll.GestorUsuarios;
import modelo.usuarios.Administrador;
import modelo.usuarios.Cliente;
import modelo.usuarios.Usuario;

import javax.swing.*;

public class MenuSuperior {

    public static JMenuBar crearMenu(JFrame ventanaActual, Usuario usuario) {

        JMenuBar barra = new JMenuBar();

        JMenu menu = new JMenu("Menú");

        // ✅ Cerrar sesión
        JMenuItem cerrarSesion = new JMenuItem("Cerrar sesión");
        cerrarSesion.addActionListener(e -> {
            ventanaActual.dispose();
            new VentanaLogin(GestorUsuarios.getInstancia()).setVisible(true);
        });
        menu.add(cerrarSesion);

        // ✅ Ir al panel de cliente (si es cliente)
        if (usuario instanceof Cliente cliente) {
            JMenuItem panelCliente = new JMenuItem("Panel de Cliente");
            panelCliente.addActionListener(e -> {
                ventanaActual.dispose();
                new VentanaCliente(cliente).setVisible(true);
            });
            menu.add(panelCliente);
        }

        // ✅ Ir al panel de administrador (si es admin)
        if (usuario instanceof Administrador admin) {
            JMenuItem panelAdmin = new JMenuItem("Panel de Administrador");
            panelAdmin.addActionListener(e -> {
                ventanaActual.dispose();
                new VentanaAdministrador(admin).setVisible(true);
            });
            menu.add(panelAdmin);
        }

        // ✅ Salir de la aplicación
        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(e -> System.exit(0));
        menu.add(salir);

        barra.add(menu);
        return barra;
    }
}


