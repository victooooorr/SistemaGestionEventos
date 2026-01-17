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
    private final GestorUsuarios gestorUsuarios;

    public VentanaLogin(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;
        setTitle("Gestión de Eventos - Acceso");
        setSize(400, 400); // Un poco más alto para que quepan bien los botones
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Aplicar estilo base
        Estilos.aplicarEstiloVentana(this);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // 1. TÍTULO
        add(Estilos.crearTitulo("Bienvenido"), BorderLayout.NORTH);

        // 2. PANEL CENTRAL (Formulario)
        JPanel panelForm = new JPanel(new GridLayout(4, 1, 10, 10));
        panelForm.setBackground(Estilos.COLOR_FONDO);
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblCorreo = new JLabel("Correo Electrónico:");
        lblCorreo.setFont(Estilos.FONT_TEXTO);
        panelForm.add(lblCorreo);

        correoField = new JTextField();
        correoField.setFont(Estilos.FONT_TEXTO);
        panelForm.add(correoField);

        JLabel lblClave = new JLabel("Contraseña:");
        lblClave.setFont(Estilos.FONT_TEXTO);
        panelForm.add(lblClave);

        claveField = new JPasswordField();
        claveField.setFont(Estilos.FONT_TEXTO);
        panelForm.add(claveField);

        add(panelForm, BorderLayout.CENTER);

        // 3. PANEL DE BOTONES (Login + Registro)
        JPanel panelBoton = new JPanel();
        panelBoton.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Gap de 10px entre botones
        panelBoton.setBackground(Estilos.COLOR_FONDO);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Botón Iniciar Sesión (Primario)
        JButton loginButton = Estilos.crearBoton("Iniciar Sesión", Estilos.COLOR_PRIMARIO);
        loginButton.addActionListener(e -> intentarLogin());
        
        // --- NUEVO: Botón Crear Cuenta (Secundario) ---
        JButton registroButton = Estilos.crearBoton("Crear Cuenta", Estilos.COLOR_SECUNDARIO);
        registroButton.addActionListener(e -> {
            // Cerramos la ventana de login actual
            this.dispose();
            // Abrimos la ventana de registro pasando el mismo gestor
            new VentanaRegistro(gestorUsuarios).setVisible(true);
        });

        panelBoton.add(loginButton);
        panelBoton.add(registroButton);

        add(panelBoton, BorderLayout.SOUTH);
    }

    private void intentarLogin() {
        String correo = correoField.getText();
        String clave = new String(claveField.getPassword());

        try {
            Usuario u = gestorUsuarios.login(correo, clave);

            if (u instanceof Cliente cliente) {
                CatalogoEventos.getInstancia().agregarObservador(cliente);
                new VentanaCliente(cliente).setVisible(true);
            } else if (u instanceof Administrador admin) {
                new VentanaAdministrador(admin).setVisible(true);
            }
            this.dispose();

        } catch (UsuarioNoEncontradoException | CredencialesIncorrectasException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Acceso", JOptionPane.ERROR_MESSAGE);
        }
    }
}