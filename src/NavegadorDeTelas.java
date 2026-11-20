import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Padrão Facade: esconde dos controllers das telas os detalhes do JavaFX
 * (FXMLLoader, Scene, Stage). Quem precisa trocar de tela só chama
 * mostrarMenu(), mostrarCriacao(), mostrarJogo() ou abrirJanela().
 */
public class NavegadorDeTelas {

    private final Stage stage;
    private final Controlador controlador;

    public NavegadorDeTelas(Stage stage, Controlador controlador) {
        this.stage = stage;
        this.controlador = controlador;
    }

    public Controlador getControlador() {
        return controlador;
    }

    public void mostrarMenu() {
        trocarTela("/menu.fxml", "Santa Aurora", 900, 670);
    }

    public void mostrarCriacao() {
        trocarTela("/criacao.fxml", "Criar protagonista", 900, 670);
    }

    /** mostra a tela da narrativa e devolve o controller dela (ou null se não foi possível abrir) */
    public JogoController mostrarJogo() {
        Object tela = trocarTela("/jogo.fxml", "Santa Aurora — Narrativa", 1050, 780);
        return (JogoController) tela;
    }

    /** abre uma janela separada (créditos, instruções) sem sair da tela atual */
    public void abrirJanela(String fxml, String titulo, int largura, int altura) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent raiz = loader.load();

            Stage janela = new Stage();
            janela.initOwner(stage);
            janela.setTitle(titulo);
            janela.setScene(new Scene(raiz, largura, altura));
            janela.show();
        } catch (IOException | RuntimeException e) {
            mostrarErro("Não foi possível abrir a tela '" + titulo + "': " + e.getMessage());
        }
    }

    public void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.OK);
        alerta.setTitle("Erro — Santa Aurora");
        alerta.setHeaderText("Não foi possível concluir a operação");
        alerta.showAndWait();
    }

    private Object trocarTela(String fxml, String titulo, int largura, int altura) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent raiz = loader.load();
            Object controllerDaTela = loader.getController();

            if (controllerDaTela instanceof Tela) {
                ((Tela) controllerDaTela).configurar(this);
            }

            stage.setTitle(titulo);
            stage.setScene(new Scene(raiz, largura, altura));
            stage.setMinWidth(640);
            stage.setMinHeight(500);
            return controllerDaTela;
        } catch (IOException | RuntimeException e) {
            mostrarErro("Não foi possível carregar a tela '" + titulo + "': " + e.getMessage());
            return null;
        }
    }
}
