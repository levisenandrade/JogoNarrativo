/**
 * Publicado pela View depois de coletar a resposta do jogador.
 *
 * Em diálogos sem opções a escolha é null: o evento significa apenas
 * "o jogador viu o diálogo, pode continuar".
 */
public class EventoEscolhaSelecionada implements Evento {

    private final Escolha escolha;
    private final Dialogo dialogoOrigem;

    public EventoEscolhaSelecionada(Escolha escolha, Dialogo dialogoOrigem) {
        this.escolha = escolha;
        this.dialogoOrigem = dialogoOrigem;
    }

    /** @return a escolha feita, ou null se o diálogo não tinha opções */
    public Escolha getEscolha() {
        return escolha;
    }

    public Dialogo getDialogoOrigem() {
        return dialogoOrigem;
    }

    public boolean temEscolha() {
        return escolha != null;
    }
}
