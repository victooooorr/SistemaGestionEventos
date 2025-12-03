package modelo.eventos;

import java.time.LocalDateTime;

public abstract class Evento {
    protected String codigo;
    protected String nombre;
    protected String tipo; // Concierto, Teatro, Conferencia, Festival
    protected LocalDateTime fechaHora;
    protected String lugar;
    protected int aforoMaximo;
    protected int aforoDisponible;
    protected double precioBase;

    public Evento(String codigo, String nombre, String tipo, LocalDateTime fechaHora,
                  String lugar, int aforoMaximo, double precioBase) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fechaHora = fechaHora;
        this.lugar = lugar;
        this.aforoMaximo = aforoMaximo;
        this.aforoDisponible = aforoMaximo;
        this.precioBase = precioBase;
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
    }

    public abstract String mostrarInfo();
}
