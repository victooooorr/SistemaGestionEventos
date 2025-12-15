package control.command;

import java.util.ArrayList;
import java.util.List;

public class Invocador {
    private final List<Comando> cola = new ArrayList<>();
    public void añadir(Comando c) { cola.add(c); }
    public void ejecutarTodos() { cola.forEach(Comando::ejecutar); cola.clear(); }
}
