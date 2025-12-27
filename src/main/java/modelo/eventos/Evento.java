package modelo.eventos;

import control.observer.SujetoEventos;
import control.observer.Observador;

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

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getLugar() { return lugar; }
    public int getAforoDisponible() { return aforoDisponible; }
    public double getPrecioBase() { return precioBase; }

    public void reducirAforo(int cantidad) {
        if (cantidad <= 0 || aforoDisponible < cantidad) {
            throw new IllegalArgumentException("No hay aforo suficiente.");
        }

        aforoDisponible -= cantidad;

        // 🔔 Notificación con mensaje personalizado
        notificarMensaje(
                "El aforo del evento '" + nombre + "' ha cambiado. Nuevo aforo: " + aforoDisponible,
                this
        );
    }

    @Override
    public void mostrarInformacion() {
        System.out.println(getNombre() + " - " + getFechaHora() + " - " + getLugar());
    }
}

