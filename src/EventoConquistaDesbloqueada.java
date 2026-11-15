/** Uma conquista acabou de ser desbloqueada. */
public class EventoConquistaDesbloqueada implements Evento {

    private final Conquista conquista;

    public EventoConquistaDesbloqueada(Conquista conquista) {
        this.conquista = conquista;
    }

    public Conquista getConquista() {
        return conquista;
    }
}
