/**
 * Base dos eventos em que o Controlador precisa de uma resposta da View
 * (confirmações, menus, listas). É a única exceção à regra de eventos imutáveis:
 * a View chama responder(...) dentro do listener e o Controlador lê a resposta
 * assim que publish(...) retorna.
 *
 * @param <R> tipo da resposta
 */
public abstract class EventoRequisicao<R> implements Evento {

    private R resposta;
    private boolean respondido = false;

    public void responder(R resposta) {
        this.resposta = resposta;
        this.respondido = true;
    }

    public boolean foiRespondido() {
        return respondido;
    }

    public R getResposta() {
        return resposta;
    }
}
