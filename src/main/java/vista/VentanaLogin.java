package vista;

import controll.CatalogoEventos;
import controll.GestorUsuarios;
import excepciones.CredencialesIncorrectasException;
import excepciones.UsuarioNoEncontradoException;
import modelo.usuarios.Administrador;
import modelo.usuarios.Cliente;
import modelo.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private JTextField correoField;
    private JPasswordField claveField;
    private JButton loginButton;

    private final GestorUsuarios gestorUsuarios;

    public VentanaLogin(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;

        setTitle("Gestión de Eventos - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }
    


    private void initComponents() {
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Correo:"));
        correoField = new JTextField();
        add(correoField);

        add(new JLabel("Clave:"));
        claveField = new JPasswordField();
        add(claveField);

        loginButton = new JButton("Acceder");
        loginButton.addActionListener(e -> intentarLogin());
        add(loginButton);
    }

    private void intentarLogin() {
        String correo = correoField.getText();
        String clave = new String(claveField.getPassword());

        try {
            Usuario u = gestorUsuarios.login(correo, clave);

            // 👉 Si es cliente
            if (u instanceof Cliente cliente) {
                CatalogoEventos.getInstancia().agregarObservador(cliente);
                JOptionPane.showMessageDialog(this, "Bienvenido " + cliente.getNombre());
                new VentanaCliente(cliente).setVisible(true);
                this.dispose();
                return;
            }

            // 👉 Si es administrador
            if (u instanceof Administrador admin) {
                JOptionPane.showMessageDialog(this, "Bienvenido Administrador " + admin.getNombre());
                new VentanaAdministrador(admin).setVisible(true);
                this.dispose();
            }

        } catch (UsuarioNoEncontradoException | CredencialesIncorrectasException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

