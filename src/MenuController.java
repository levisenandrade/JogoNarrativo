import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    private Controlador controlador;

    /** chamado pela App logo depois de carregar o FXML */
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    /** chamado automaticamente pelo JavaFX quando os componentes do FXML já foram criados */
    @FXML
    private void initialize() {
        System.out.println("Janela do menu carregada.");
    }

    @FXML
    private void aoApertarSair() {
        Platform.exit();
    }

    @FXML
    private void aoMostrarCreditos() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/creditos.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Créditos");
            stage.setScene(new Scene(root, 600, 500));
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de créditos.");
            e.printStackTrace();
        }
    }
    @FXML
    private void aoMostrarInstrucao() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/instrucoes.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Intruções");
            stage.setScene(new Scene(root, 730, 500));
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de instruções.");
            e.printStackTrace();
        }
    }
    @FXML
    private void aoIniciarOJogo(ActionEvent event) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/jogo.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Jogo");

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela do jogo.");
            e.printStackTrace();
        }
    }
}