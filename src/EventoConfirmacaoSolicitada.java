/** Pergunta de sim ou não. Resposta: true se o jogador confirmou. */
public class EventoConfirmacaoSolicitada extends EventoRequisicao<Boolean> {

    private final String pergunta;

    public EventoConfirmacaoSolicitada(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getPergunta() {
        return pergunta;
    }
}
