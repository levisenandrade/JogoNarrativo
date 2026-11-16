
public class EventoEscolhaRealizada implements Evento {

    private final Dialogo dialogo;
    private final Escolha escolha;

    public EventoEscolhaRealizada(
            Dialogo dialogo,
            Escolha escolha
    ) {
        this.dialogo = dialogo;
        this.escolha = escolha;
    }

    public Dialogo getDialogo() {
        return dialogo;
    }

    public Escolha getEscolha() {
        return escolha;
    }
}
