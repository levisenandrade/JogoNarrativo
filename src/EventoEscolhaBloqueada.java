/**
 * A escolha feita está bloqueada. A View mostra o motivo, pede outra opção
 * e publica um novo EventoEscolhaSelecionada.
 */
public class EventoEscolhaBloqueada implements Evento {

    private final Dialogo dialogo;
    private final String motivo;

    public EventoEscolhaBloqueada(Dialogo dialogo, String motivo) {
        this.dialogo = dialogo;
        this.motivo = motivo;
    }

    public Dialogo getDialogo() {
        return dialogo;
    }

    public String getMotivo() {
        return motivo;
    }
}
