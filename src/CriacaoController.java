import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Tela de criação do protagonista (criacao.fxml). Valida os campos digitados e entrega
 * ao Controlador; a criação em si (e a pergunta do personagem favorito) é do Controlador.
 */
public class CriacaoController implements Tela {

    @FXML private TextField campoNome;
    @FXML private TextField campoIdade;
    @FXML private TextField campoGenero;
    @FXML private Label rotuloErro;

    private NavegadorDeTelas navegador;

    @Override
    public void configurar(NavegadorDeTelas navegador) {
        this.navegador = navegador;
    }

    @FXML
    private void aoComecar() {
        String nome = campoNome.getText().trim();
        String genero = campoGenero.getText().trim();

        if (nome.isEmpty()) {
            rotuloErro.setText("O nome não pode ficar vazio.");
            return;
        }

        int idade;
        try {
            idade = Integer.parseInt(campoIdade.getText().trim());
        } catch (NumberFormatException e) {
            rotuloErro.setText("Digite a idade como um número inteiro.");
            return;
        }

        if (idade <= 0) {
            rotuloErro.setText("A idade deve ser maior que zero.");
            return;
        }

        if (genero.isEmpty()) {
            rotuloErro.setText("O gênero não pode ficar vazio.");
            return;
        }

        Controlador controlador = navegador.getControlador();
        Protagonista protagonista = controlador.criarProtagonista(nome, idade, genero, null);
        controlador.executarHistoria(EstadoPartida.nova(protagonista));
    }

    @FXML
    private void aoVoltar() {
        navegador.mostrarMenu();
    }
}
