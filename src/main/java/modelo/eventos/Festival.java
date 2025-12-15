package modelo.eventos;

import java.time.LocalDateTime;
import java.util.*;

public class Festival extends Evento {
    private Map<String, List<String>> horariosPorDia;

    public Festival(String codigo, String nombre, LocalDateTime fechaHora, String lugar,
                    int aforoMaximo, double precioBase, String urlInfo) {
        super(codigo, nombre, "Festival", fechaHora, lugar, aforoMaximo, precioBase, urlInfo);
        this.horariosPorDia = new HashMap<>();
    }

    public void agregarHorario(String dia, String horario) {
        horariosPorDia.computeIfAbsent(dia, k -> new ArrayList<>()).add(horario);
    }

    @Override
    public String mostrarInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] %s %s | Precio: %.2f€ | Info: %s\n", tipo, nombre, lugar, precioBase, urlInfo));
        horariosPorDia.forEach((dia, horarios) -> {
            sb.append("  ").append(dia).append(": ").append(String.join(", ", horarios)).append("\n");
        });
        return sb.toString();
    }
}

