package vista;

import controll.GestorUsuarios;
import modelo.usuarios.Cliente;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaRegistro extends JFrame {

    private final GestorUsuarios gestorUsuarios;
    
    // Campos del formulario (coinciden con el constructor de Cliente)
    private JTextField txtNif, txtNombre, txtEmail, txtTelefono, txtFecha;
    private JPasswordField txtPassword;

    public VentanaRegistro(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;

        setTitle("Registro de Nuevo Usuario");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // No cierra la app, solo la ventana
        Estilos.aplicarEstiloVentana(this);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // --- TÍTULO ---
        add(Estilos.crearTitulo("Crear Cuenta"), BorderLayout.NORTH);

        // --- FORMULARIO ---
        JPanel panelForm = new JPanel(new GridLayout(0, 1, 5, 5)); // Una columna, filas dinámicas
        panelForm.setBackground(Estilos.COLOR_FONDO);
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Añadimos los campos usando un método auxiliar
        agregarCampo(panelForm, "NIF / DNI:", txtNif = new JTextField());
        agregarCampo(panelForm, "Nombre Completo:", txtNombre = new JTextField());
        agregarCampo(panelForm, "Email:", txtEmail = new JTextField());
        agregarCampo(panelForm, "Teléfono:", txtTelefono = new JTextField());
        agregarCampo(panelForm, "Fecha Nacimiento (YYYY-MM-DD):", txtFecha = new JTextField());
        
        panelForm.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panelForm.add(txtPassword);

        add(panelForm, BorderLayout.CENTER);

        // --- BOTONES ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(Estilos.COLOR_FONDO);

        JButton btnRegistrar = Estilos.crearBoton("Registrarse", Estilos.COLOR_PRIMARIO);
        JButton btnVolver = Estilos.crearBoton("Volver", Color.GRAY);

        // Lógica de Registro
        btnRegistrar.addActionListener(e -> registrarUsuario());

        // Lógica de Volver (Abre el Login de nuevo)
        btnVolver.addActionListener(e -> {
            new VentanaLogin(gestorUsuarios).setVisible(true);
            dispose();
        });

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel panel, String label, JTextField campo) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(Estilos.FONT_BOLD);
        panel.add(lbl);
        panel.add(campo);
    }

    private void registrarUsuario() {
        // 1. Recoger datos
        String nif = txtNif.getText().trim();
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String tlf = txtTelefono.getText().trim();
        String fechaStr = txtFecha.getText().trim();
        String password = new String(txtPassword.getPassword());

        // 2. Validaciones básicas
        if (nif.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos.");
            return;
        }

        try {
            // 3. Convertir fecha
            LocalDate fechaNacimiento = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // 4. Crear el objeto Cliente (Igual que en tu Main)
            Cliente nuevoCliente = new Cliente(nif, nombre, email, password, tlf, fechaNacimiento);

            // 5. Guardar en el Gestor (Singleton)
            // Nota: Según tu Main, el método altaUsuario pide el objeto Y la contraseña aparte
            gestorUsuarios.altaUsuario(nuevoCliente, password);

            // 6. Éxito y redirección
            JOptionPane.showMessageDialog(this, "¡Cuenta creada con éxito! Ahora puedes iniciar sesión.");
            
            new VentanaLogin(gestorUsuarios).setVisible(true);
            dispose(); // Cerrar ventana de registro

        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Usa: YYYY-MM-DD (Ej: 1995-05-20)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
            // Posiblemente salte si el usuario ya existe, dependiendo de tu GestorUsuarios
        }
    }
}