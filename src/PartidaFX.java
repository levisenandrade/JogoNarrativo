import javafx.application.Application;

/**
 * Ponto de entrada da versão gráfica (equivalente gráfico da Partida).
 *
 * Fica separada da App de propósito: se a classe que tem o main estender Application,
 * rodar pelo classpath dá o erro "JavaFX runtime components are missing".
 */
public class PartidaFX {
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
