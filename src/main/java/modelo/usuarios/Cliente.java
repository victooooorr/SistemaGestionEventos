package modelo.usuarios;

import control.observer.Observador;
import java.time.LocalDate;
import modelo.eventos.Evento;

public class Cliente extends Usuario implements Observador {
    private Preferencias preferencias;
    private LocalDate fechaNacimiento;

    public Cliente(String dni, String nombre, String correo, String clave, String telefono, LocalDate fechaNacimiento) {
        super(dni, nombre, correo, clave, telefono);
        this.fechaNacimiento = fechaNacimiento;
    }
     public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setPreferencias(Preferencias preferencias) { this.preferencias = preferencias; }
    public Preferencias getPreferencias() { return preferencias; }

    public void actualizar(Evento e) {
        if (preferencias != null && preferencias.coincide(e)) {
            System.out.printf("Notificación para %s: nuevo evento %s%n", nombre, e.mostrarInfo());
        }
    }
}
