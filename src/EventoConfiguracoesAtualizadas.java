/** As configurações mudaram (ou foram carregadas): a View ajusta o que depende delas. */
public class EventoConfiguracoesAtualizadas implements Evento {

    private final Configuracoes configuracoes;

    public EventoConfiguracoesAtualizadas(Configuracoes configuracoes) {
        this.configuracoes = configuracoes;
    }

    public Configuracoes getConfiguracoes() {
        return configuracoes;
    }
}
