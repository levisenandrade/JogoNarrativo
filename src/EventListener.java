    /**
 * Quem quiser reagir a um tipo de evento implementa esta interface.
 *
 * @param <T> tipo de evento tratado
 */
public interface EventListener<T extends Evento> {
    void onEvent(T evento);
}
