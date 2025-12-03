package modelo.usuarios;

public abstract class Usuario {
    protected String dni;
    protected String nombre;
    protected String correo;
    protected String clave;
    protected String telefono;

    public Usuario(String dni, String nombre, String correo, String clave, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.telefono = telefono;
    }

    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
}
