/**
 * Indica que um diálogo está pronto para ser mostrado ao jogador.
 */
public class EventoDialogoDisponivel implements Evento {

    private final Dialogo dialogo;

    public EventoDialogoDisponivel(Dialogo dialogo) {
        this.dialogo = dialogo;
    }

    public Dialogo getDialogo() {
        return dialogo;
    }
}
