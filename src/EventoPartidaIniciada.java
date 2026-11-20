/** A View deve preparar a tela gráfica antes de a narrativa apresentar seu primeiro diálogo. */
public class EventoPartidaIniciada implements Evento {
    private final EstadoPartida estado;
    public EventoPartidaIniciada(EstadoPartida estado) { this.estado = estado; }
    public EstadoPartida getEstado() { return estado; }
}
