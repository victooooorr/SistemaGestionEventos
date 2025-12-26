package modelo.eventos;

public class HorarioFestival implements ComponenteEvento {

    private final String nombre;
    private final String horario;

    public HorarioFestival(String nombre, String horario) {
        this.nombre = nombre;
        this.horario = horario;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println(" - " + nombre + " | Horario: " + horario);
    }

    public String getHorario() {
        return horario;
    }
    @Override
    public String toString() {
        return nombre + " — " + horario;
    }

}
