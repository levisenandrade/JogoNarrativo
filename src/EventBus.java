import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Canal de comunicação desacoplada entre Controlador e View.
 *
 * - Vários listeners podem assinar o mesmo tipo de evento.
 * - A entrega é síncrona e na ordem de publicação (FIFO), sem threads.
 * - Se um listener publicar outro evento durante o tratamento, o novo evento
 *   entra na fila e só é entregue depois que o listener atual terminar.
 *   Assim a pilha de chamadas não cresce a cada diálogo da história.
 * - O tipo do evento é casado pela classe exata (sem herança entre eventos).
 */
public class EventBus {

    private final Map<Class<? extends Evento>, List<Consumer<Evento>>> listeners = new HashMap<>();
    private final Queue<Evento> fila = new ArrayDeque<>();
    private boolean despachando = false;

    /**
     * Registra um listener para um tipo de evento.
     *
     * @param tipo     classe do evento (ex.: EventoCenaIniciada.class)
     * @param listener quem será chamado quando o evento for publicado
     */
    public <T extends Evento> void subscribe(Class<T> tipo, EventListener<T> listener) {
        if (tipo == null || listener == null) {
            throw new IllegalArgumentException("tipo e listener não podem ser nulos");
        }

        listeners.computeIfAbsent(tipo, k -> new ArrayList<>())
                .add(evento -> listener.onEvent(tipo.cast(evento)));
    }

    /**
     * Publica um evento para todos os listeners inscritos nele.
     * Se já houver uma entrega em andamento, o evento aguarda na fila.
     */
    public void publish(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("evento não pode ser nulo");
        }

        fila.add(evento);

        if (despachando) {
            return;
        }

        despachando = true;
        try {
            while (!fila.isEmpty()) {
                despachar(fila.poll());
            }
        } finally {
            despachando = false;
            fila.clear(); // só sobra algo se um listener lançou exceção
        }
    }

    /**
     * Publica um pedido e devolve a resposta dada pela View.
     * A entrega é imediata (não passa pela fila), porque o Controlador precisa da
     * resposta para continuar o que estava fazendo.
     *
     * @throws IllegalStateException se nenhuma View respondeu ao pedido
     */
    public <R> R request(EventoRequisicao<R> pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("pedido não pode ser nulo");
        }

        despachar(pedido);

        if (!pedido.foiRespondido()) {
            throw new IllegalStateException("Nenhuma View respondeu ao pedido: " + pedido.getClass().getSimpleName());
        }

        return pedido.getResposta();
    }

    private void despachar(Evento evento) {
        List<Consumer<Evento>> inscritos = listeners.get(evento.getClass());

        if (inscritos == null) {
            return;
        }

        // cópia: um listener pode se inscrever durante a entrega
        for (Consumer<Evento> inscrito : new ArrayList<>(inscritos)) {
            inscrito.accept(evento);
        }
    }
}
