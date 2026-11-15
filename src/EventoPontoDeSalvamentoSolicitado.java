/** Mostrar o menu do ponto de salvamento. Resposta: 1 = continuar, 2 = salvar, 3 = voltar ao menu. */
public class EventoPontoDeSalvamentoSolicitado extends EventoRequisicao<Integer> {

    private final String rotuloDoAto;

    public EventoPontoDeSalvamentoSolicitado(String rotuloDoAto) {
        this.rotuloDoAto = rotuloDoAto;
    }

    public String getRotuloDoAto() {
        return rotuloDoAto;
    }
}
