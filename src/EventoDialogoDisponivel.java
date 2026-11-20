/** Um diálogo está pronto: id e posição permitem carregar a arte da cena. */
public class EventoDialogoDisponivel implements Evento {
    private final Dialogo dialogo;
    private final String cenaId;
    private final int indice;

    public EventoDialogoDisponivel(Dialogo dialogo) { this(dialogo, null, 0); }

    public EventoDialogoDisponivel(Dialogo dialogo, String cenaId, int indice) {
        this.dialogo = dialogo;
        this.cenaId = cenaId;
        this.indice = indice;
    }
    public Dialogo getDialogo() { return dialogo; }
    public String getCenaId() { return cenaId; }
    /** Índice começando em 1; zero indica que não há posição conhecida. */
    public int getIndice() { return indice; }
}
