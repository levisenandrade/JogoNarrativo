/**
 * Apenas repassa a escolha do jogador ao Controlador.
 * Toda a decisão de fluxo continua no Controlador.
 */
public class EscolhaSelecionadaListener implements EventListener<EventoEscolhaSelecionada> {

    private final Controlador controlador;

    public EscolhaSelecionadaListener(Controlador controlador) {
        this.controlador = controlador;
    }

    public void onEvent(EventoEscolhaSelecionada evento) {
        controlador.processarEscolha(evento.getEscolha(), evento.getDialogoOrigem());
    }
}
