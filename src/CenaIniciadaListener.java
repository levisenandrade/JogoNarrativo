import java.util.List;

/**
 * Quando uma cena começa, libera o primeiro diálogo dela.
 * Não exibe nada: só publica o próximo evento.
 */
public class CenaIniciadaListener implements EventListener<EventoCenaIniciada> {

    private final EventBus eventBus;

    public CenaIniciadaListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void onEvent(EventoCenaIniciada evento) {
        List<Dialogo> dialogos = evento.getCena().getDialogos();

        if (dialogos.isEmpty()) {
            return; // cena sem diálogos: nada a mostrar
        }

        eventBus.publish(new EventoDialogoDisponivel(dialogos.get(0)));
    }
}
