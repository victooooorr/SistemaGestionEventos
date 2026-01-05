package modelo.usuarios;

import control.observer.Observador;
import modelo.eventos.Evento;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario implements Observador {

    private LocalDate fechaNacimiento;
    private Preferencias preferencias;
    private boolean recibeNotificaciones = true; // --- NUEVO: Por defecto activadas ---

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
    
    // --- NUEVOS MÉTODOS PARA CONTROLAR NOTIFICACIONES ---
    public void setRecibeNotificaciones(boolean recibe) {
        this.recibeNotificaciones = recibe;
    }
    public boolean isRecibeNotificaciones() {
        return recibeNotificaciones;
    }

    private List<String> notificaciones = new ArrayList<>();
    public List<String> getNotificaciones() {
        return notificaciones;
    }

    @Override
    public void actualizar(Evento e) {
        // Si el cliente desactivó notificaciones, no hacemos nada
        if (!recibeNotificaciones) return;

        if (preferencias != null && preferencias.coincide(e)) {
            String mensaje = "Nuevo evento recomendado: " + e.getNombre();
            notificaciones.add(mensaje);
        }
    }
    
    @Override
    public void actualizarMensaje(String mensaje, Evento e) {
        if (!recibeNotificaciones) return;
        notificaciones.add(mensaje);
    }
}