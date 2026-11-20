import javafx.application.Platform;
import javafx.fxml.FXML;

/**
 * Tela do menu inicial (menu.fxml). Só encaminha os cliques: abre telas pelo NavegadorDeTelas
 * e pede ao Controlador tudo que é regra do jogo (saves, configurações, conquistas, partida).
 */
public class MenuController implements Tela {

    private NavegadorDeTelas navegador;

    @Override
    public void configurar(NavegadorDeTelas navegador) {
        this.navegador = navegador;
    }

    @FXML
    private void aoIniciarOJogo() {
        Controlador controlador = navegador.getControlador();

        // se houver personagem favorito, o Controlador pergunta (janela de confirmação) se quer usá-lo
        Protagonista favorito = controlador.perguntaUsarFavorito();

        if (favorito != null) {
            controlador.executarHistoria(EstadoPartida.nova(favorito));
        } else {
            navegador.mostrarCriacao();
        }
    }

    @FXML
    private void aoContinuar() {
        navegador.getControlador().continuarPartida();
    }

    @FXML
    private void aoSaves() {
        navegador.getControlador().gerenciarSaves();
    }

    @FXML
    private void aoConquistas() {
        navegador.getControlador().mostrarConquistas();
    }

    @FXML
    private void aoConfiguracoes() {
        navegador.getControlador().gerenciarConfiguracoes();
    }

    @FXML
    private void aoMostrarCreditos() {
        navegador.abrirJanela("/creditos.fxml", "Créditos", 650, 540);
    }

    @FXML
    private void aoMostrarInstrucao() {
        navegador.abrirJanela("/instrucoes.fxml", "Instruções", 780, 560);
    }

    @FXML
    private void aoApertarSair() {
        Platform.exit();
    }
}
