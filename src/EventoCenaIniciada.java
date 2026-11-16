/**
 * Publicado pelo Controlador quando uma cena começa.
 */
public class EventoCenaIniciada implements Evento {

    private final Cena cena;

    public EventoCenaIniciada(Cena cena) {
        this.cena = cena;
    }

    public Cena getCena() {
        return cena;
    }
}
