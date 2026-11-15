/**
 * Classe responsável por iniciar a execução do jogo.
 *
 * É também onde as peças do MVC são montadas: o EventBus é o canal de comunicação entre a View e
 * o Controlador. O Menu (View) recebe a Entrada (leitura do teclado) e o EventBus, e o Controlador
 * recebe o EventBus e os repositórios (saves, progresso e configurações). Cada um registra no
 * EventBus os eventos que sabe tratar; um não conhece o outro, só os eventos.
 *
 * @author Levi Sena Andrade
 */

public class Partida {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();

        Entrada entrada = new Entrada();
        Menu menu = new Menu(entrada, eventBus);
        menu.registrarNoBus();

        RepositorioDeSaves repositorio = new RepositorioJson();
        RepositorioDeProgresso repositorioDeProgresso = new RepositorioDeProgresso();
        RepositorioDeConfiguracoes repositorioDeConfiguracoes = new RepositorioDeConfiguracoes();
        Controlador controlador = new Controlador(eventBus, repositorio, repositorioDeProgresso, repositorioDeConfiguracoes);
        controlador.registrarListeners();

        // o Menu aciona o Controlador só para as ações do menu principal (ele é quem lê o teclado)
        menu.setControlador(controlador);

        try {
            menu.menuInicial();
        } catch (IllegalStateException e) {
            // a entrada de dados acabou (ex.: Ctrl+D): encerra sem mostrar erro técnico
            System.out.println("\nEntrada encerrada. Saindo do jogo.");
        }
    }
}
