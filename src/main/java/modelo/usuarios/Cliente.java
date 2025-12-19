package modelo.usuarios;

import control.observer.Observador;
import modelo.eventos.Evento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario implements Observador {

    private LocalDate fechaNacimiento;
    private Preferencias preferencias;

    public Cliente(String dni, String nombre, String correo, String clave, String telefono,
                   LocalDate fechaNacimiento) {
        super(dni, nombre, correo, clave, telefono);
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setPreferencias(Preferencias preferencias) {
        this.preferencias = preferencias;
    }

    public Preferencias getPreferencias() {
        return preferencias;
    }
    private List<String> notificaciones = new ArrayList<>();

    public List<String> getNotificaciones() {
        return notificaciones;
    }

    @Override
    public void actualizar(Evento e) {
        if (preferencias != null && preferencias.coincide(e)) {
            String mensaje = "Nuevo evento recomendado: " + e.getNombre();
            notificaciones.add(mensaje);
            System.out.println("🔔 " + mensaje);
        }
    
}
}
    

