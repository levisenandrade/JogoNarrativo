/**
 * Toda tela JavaFX que precisa falar com o resto do jogo implementa esta interface.
 * O NavegadorDeTelas chama configurar() logo depois de carregar o FXML.
 */
public interface Tela {
    void configurar(NavegadorDeTelas navegador);
}
