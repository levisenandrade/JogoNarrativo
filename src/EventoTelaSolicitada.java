/** Pedido para mostrar uma tela informativa (sem resposta). */
public class EventoTelaSolicitada implements Evento {

    public enum Tela { INSTRUCOES, CREDITOS, SAIDA }

    private final Tela tela;

    public EventoTelaSolicitada(Tela tela) {
        this.tela = tela;
    }

    public Tela getTela() {
        return tela;
    }
}
