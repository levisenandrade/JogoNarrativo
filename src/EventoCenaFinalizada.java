
public class EventoCenaFinalizada implements Evento {

    private final Cena cena;

    public EventoCenaFinalizada(Cena cena) {
        this.cena = cena;
    }

    public Cena getCena() {
        return cena;
    }
}
