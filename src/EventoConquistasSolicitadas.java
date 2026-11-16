/** Mostrar a tela de conquistas. Resposta: 1 = redefinir, 0 = voltar. */
public class EventoConquistasSolicitadas extends EventoRequisicao<Integer> {

    private final ProgressoGlobal progresso;

    public EventoConquistasSolicitadas(ProgressoGlobal progresso) {
        this.progresso = progresso;
    }

    public ProgressoGlobal getProgresso() {
        return progresso;
    }
}
