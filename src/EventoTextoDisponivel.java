/**
 * Avisa a View que há um texto para ser mostrado ao jogador
 * (substitui as chamadas diretas a menu.mostraString).
 */
public class EventoTextoDisponivel implements Evento {

    private final String texto;

    public EventoTextoDisponivel(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}
