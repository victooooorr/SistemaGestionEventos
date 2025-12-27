package control.observer;

import modelo.eventos.Evento;

public interface Observador {

    void actualizar(Evento evento);

    // Nuevo método para mensajes personalizados
    default void actualizarMensaje(String mensaje, Evento evento) {}
}
