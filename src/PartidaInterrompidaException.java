/**
 * Lançada pela View quando o jogador escolhe voltar ao menu no meio da história.
 *
 * Ela "desmonta" a execução da história (que estava esperando uma resposta do jogador)
 * até o Controlador, que a trata em executarHistoria e encerra a partida normalmente.
 */
public class PartidaInterrompidaException extends RuntimeException {
    public PartidaInterrompidaException() {
        super("A partida foi encerrada pelo jogador.");
    }
}
