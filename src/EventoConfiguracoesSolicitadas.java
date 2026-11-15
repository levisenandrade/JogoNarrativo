/** Mostrar a tela de configurações. Resposta: opção escolhida (0 = voltar). */
public class EventoConfiguracoesSolicitadas extends EventoRequisicao<Integer> {

    private final Configuracoes configuracoes;

    public EventoConfiguracoesSolicitadas(Configuracoes configuracoes) {
        this.configuracoes = configuracoes;
    }

    public Configuracoes getConfiguracoes() {
        return configuracoes;
    }
}
