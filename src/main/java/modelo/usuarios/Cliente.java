package modelo.usuarios;

import control.observer.Observador;
import modelo.eventos.Evento;

public class Cliente extends Usuario implements Observador {
    private Preferencias preferencias;

    public Cliente(String dni, String nombre, String correo, String clave, String telefono) {
        super(dni, nombre, correo, clave, telefono);
    }

    public void setPreferencias(Preferencias preferencias) { this.preferencias = preferencias; }
    public Preferencias getPreferencias() { return preferencias; }

    public void actualizar(Evento e) {
        if (preferencias != null && preferencias.coincide(e)) {
            System.out.printf("Notificación para %s: nuevo evento %s%n", nombre, e.mostrarInfo());
        }
    }
}
