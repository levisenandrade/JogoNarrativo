import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class InstrucoesController {

    @FXML
    private void aoVoltar(ActionEvent event) {

        // fecha apenas a janela dos créditos
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }
}