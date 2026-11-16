
import java.util.List;

public class EventoEscolhasApresentadas implements Evento {

    private final Dialogo dialogo;
    private final List<Escolha> opcoes;

    public EventoEscolhasApresentadas(
            Dialogo dialogo,
            List<Escolha> opcoes
    ) {
        this.dialogo = dialogo;
        this.opcoes = List.copyOf(opcoes);
    }

    public Dialogo getDialogo() {
        return dialogo;
    }

    public List<Escolha> getOpcoes() {
        return opcoes;
    }
}
