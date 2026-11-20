import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Aplicação JavaFX. Monta as mesmas peças que a Partida (terminal) monta, mas com a View gráfica
 * no lugar do Menu: Model e Controlador não mudam.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        EventBus eventBus = new EventBus();

        Controlador controlador = new Controlador(eventBus, new RepositorioJson(),
                new RepositorioDeProgresso(), new RepositorioDeConfiguracoes());
        controlador.registrarListeners();

        NavegadorDeTelas navegador = new NavegadorDeTelas(stage, controlador);

        // a View precisa estar inscrita no bus antes de o Controlador publicar o primeiro evento
        ViewFX view = new ViewFX(eventBus, navegador);
        view.registrarNoBus();

        controlador.inicializar(); // lê configurações e progresso gravados

        // fechar a janela no meio da história encerra o programa (o salvamento automático já foi feito)
        stage.setOnCloseRequest(evento -> System.exit(0));

        navegador.mostrarMenu();
        stage.show();
    }
}
