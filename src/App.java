import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Aplicação JavaFX. Monta as mesmas peças que a Partida monta,
 * mas em vez de chamar controlador.menuInicial() (loop de terminal), abre a janela.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1. mesma montagem da Partida (Model e Controlador não mudam)
        Entrada entrada = new Entrada();
        Menu menu = new Menu(entrada); // View de terminal: ainda usada pelo Controlador nas partes não migradas
        RepositorioDeSaves repositorio = new RepositorioJson();
        RepositorioDeProgresso repositorioDeProgresso = new RepositorioDeProgresso();
        RepositorioDeConfiguracoes repositorioDeConfiguracoes = new RepositorioDeConfiguracoes();
        Controlador controlador = new Controlador(menu, repositorio, repositorioDeProgresso, repositorioDeConfiguracoes);

        // 2. carrega configurações e progresso (o que o menuInicial() fazia antes do loop)
        controlador.inicializar();

        // 3. carrega a nova View (FXML) e entrega o Controlador ao controller da tela
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Parent raiz = loader.load();
        MenuController menuController = loader.getController();
        menuController.setControlador(controlador);

        stage.setTitle("Jogo Narrativo Interativo");
        stage.setScene(new Scene(raiz, 800, 600));
        stage.show();
    }
}