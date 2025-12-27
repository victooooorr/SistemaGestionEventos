package modelo.eventos;

import control.observer.SujetoEventos;
import control.observer.Observador;
import controll.GestorNotificaciones;

import java.time.LocalDateTime;

public abstract class Evento extends SujetoEventos implements ComponenteEvento {

    protected String codigo;
    protected String nombre;
    protected String tipo;
    protected LocalDateTime fechaHora;
    protected String lugar;
    protected int aforoMaximo;
    protected int aforoDisponible;
    protected double precioBase;
    protected String urlInfo;

    public Evento(String codigo, String nombre, String tipo, LocalDateTime fechaHora,
                  String lugar, int aforoMaximo, double precioBase, String urlInfo) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fechaHora = fechaHora;
        this.lugar = lugar;
        this.aforoMaximo = aforoMaximo;
        this.aforoDisponible = aforoMaximo;
        this.precioBase = precioBase;
        this.urlInfo = urlInfo;
    }
    public abstract Evento clonarConNuevosDatos(
        String nombre,
        LocalDateTime fecha,
        String lugar,
        double precio,
        int aforo
);


    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getLugar() { return lugar; }
    public int getAforoDisponible() { return aforoDisponible; }
    public double getPrecioBase() { return precioBase; }
    public String getUrlInfo(){ return urlInfo; }
    public void reducirAforo(int cantidad) {
    if (cantidad <= 0 || aforoDisponible < cantidad) {
        throw new IllegalArgumentException("No hay aforo suficiente.");
    }

    aforoDisponible -= cantidad;

    String mensaje = "El aforo del evento '" + nombre + "' ha cambiado. Nuevo aforo: " + aforoDisponible;

    // 🔥 Guardar notificación global
    GestorNotificaciones.agregar(mensaje);

    // 🔔 Notificar a observadores
    notificarMensaje(mensaje, this);
}


    @Override
    public void mostrarInformacion() {
        System.out.println(getNombre() + " - " + getFechaHora() + " - " + getLugar());
    }
}

