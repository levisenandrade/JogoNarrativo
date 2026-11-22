import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * Tela da narrativa (jogo.fxml). É só View: mostra cena, personagem, texto e opções
 * e devolve o número do botão clicado. Quem decide o que acontece depois é o Controlador.
 *
 * Como o Controlador espera a resposta do jogador dentro do loop da história, esta tela
 * "segura" a execução com Platform.enterNestedEventLoop até o jogador clicar; o clique
 * chama exitNestedEventLoop e a história continua de onde parou. Tudo roda na mesma thread.
 */
public class JogoController implements Tela {
    @FXML private Label rotuloAto;
    @FXML private Label rotuloPersonagem;
    @FXML private Label rotuloTexto;
    @FXML private Label rotuloAviso;
    @FXML private Label rotuloImagemPendente;
    @FXML private VBox caixaDeOpcoes;
    @FXML private Button botaoContinuar;
    @FXML private ScrollPane painelDeTexto;
    @FXML private ScrollPane painelDeOpcoes;
    @FXML private ImageView imagemCena;

    private final Object chaveDeEspera = new Object();
    private boolean esperando;      // true enquanto a história aguarda um clique
    private boolean interrompida;   // true se o jogador escolheu voltar ao menu
    private int opcaoClicada;

    private EstadoPartida estado;
    private String cenaAtual;

    @Override
    public void configurar(NavegadorDeTelas navegador) {
        // esta tela não navega sozinha: quem troca de tela ao fim da partida é a ViewFX
    }

    public void definirEstado(EstadoPartida estado) {
        this.estado = estado;
        atualizarCabecalho();
    }

    public void definirCena(String id) {
        cenaAtual = id;
        atualizarCabecalho();
    }

    private void atualizarCabecalho() {
        if (estado == null) {
            return;
        }

        String cena = cenaAtual == null ? "" : "  ·  " + cenaAtual.replace('_', ' ');
        rotuloAto.setText(estado.getAto().getRotulo() + cena);
    }

    // ---------------------------------------------------------------- o que a ViewFX pede para mostrar

    /**
     * mostra um diálogo da história
     *
     * @param aviso  mensagem extra em destaque (ex.: motivo de uma escolha bloqueada) ou null
     * @param indice posição do diálogo na cena (usada para trocar a arte)
     * @return o número da opção escolhida, ou 0 se o diálogo não tem opções
     */
    public int mostrarDialogo(Dialogo dialogo, String aviso, int indice) {
        String personagem = "";

        if (dialogo.getPersonagem() != null && !dialogo.getPersonagem().getNome().equalsIgnoreCase("Narrador")) {
            personagem = dialogo.getPersonagem().getNome();
        }

        atualizarCabecalho();

        if (indice > 0) { // 0 = escolha bloqueada: o jogador escolhe de novo e a imagem continua a mesma
            atualizarArte(indice);
        }

        String[] alternativas = null;

        if (dialogo.possuiOpcoes()) {
            Escolha[] escolhas = dialogo.getEscolhas();
            alternativas = new String[escolhas.length];

            for (int i = 0; i < escolhas.length; i++) {
                alternativas[i] = escolhas[i].getTexto();
            }
        }

        return exibir(personagem, dialogo.getTexto(), aviso, alternativas);
    }

    /** mostra uma mensagem do jogo (consequência de escolha, save feito...) e espera o jogador continuar */
    public void mostrarAviso(String texto) {
        exibir("", texto, null, null);
    }

    /**
     * mostra o ponto de salvamento do início de cada ato
     *
     * @return 1 para continuar, 2 para salvar, 3 para voltar ao menu principal
     */
    public int mostrarPontoDeSalvamento(String rotulo) {
        rotuloAto.setText(rotulo);

        String[] opcoes = {"Continuar a história", "Salvar manualmente", "Voltar ao menu principal"};
        return exibir("", "Ponto de salvamento — " + rotulo, null, opcoes);
    }

    // ---------------------------------------------------------------- espera pelo clique

    /** monta a tela e espera o clique. Sem opções, mostra o botão Continuar e devolve 0. */
    private int exibir(String personagem, String texto, String aviso, String[] opcoes) {
        rotuloPersonagem.setText(personagem == null ? "" : personagem);
        rotuloTexto.setText(texto == null ? "" : texto);
        rotuloAviso.setText(aviso == null ? "" : aviso);
        painelDeTexto.setVvalue(0);
        painelDeOpcoes.setVvalue(0);
        caixaDeOpcoes.getChildren().clear();

        boolean semOpcoes = (opcoes == null || opcoes.length == 0);
        botaoContinuar.setVisible(semOpcoes);
        botaoContinuar.setManaged(semOpcoes);
        painelDeOpcoes.setVisible(!semOpcoes);
        painelDeOpcoes.setManaged(!semOpcoes);

        if (!semOpcoes) {
            for (int i = 0; i < opcoes.length; i++) {
                final int numero = i + 1;

                Button botao = new Button(opcoes[i]);
                botao.setWrapText(true);
                botao.setMaxWidth(Double.MAX_VALUE);
                botao.setMinHeight(Region.USE_PREF_SIZE);
                botao.getStyleClass().add("botao-escolha");
                botao.setOnAction(evento -> responder(numero));

                caixaDeOpcoes.getChildren().add(botao);
            }
        }

        return aguardarResposta();
    }

    private int aguardarResposta() {
        if (interrompida) {
            throw new PartidaInterrompidaException();
        }

        opcaoClicada = 0;
        esperando = true;
        Platform.enterNestedEventLoop(chaveDeEspera); // só volta quando alguém chamar exitNestedEventLoop

        if (interrompida) {
            throw new PartidaInterrompidaException();
        }

        return opcaoClicada;
    }

    private void responder(int numero) {
        if (!esperando) {
            return; // clique repetido enquanto a história já está continuando
        }

        esperando = false;
        opcaoClicada = numero;
        Platform.exitNestedEventLoop(chaveDeEspera, null);
    }

    @FXML
    private void aoContinuar() {
        responder(0);
    }

    // ---------------------------------------------------------------- botões do topo

    @FXML
    private void aoStatus() {
        if (estado == null) {
            return;
        }

        Protagonista protagonista = estado.getProtagonista();

        String informacoes = protagonista.getNome() + "  ·  " + protagonista.getIdade() + " anos\n\n";

        for (String atributo : protagonista.getAtributos().keySet()) {
            informacoes = informacoes + atributo + ": " + protagonista.getAtributo(atributo) + "\n";
        }

        informacoes = informacoes + "\nRelacionamentos\n"
                + "Jonas: " + estado.getJonas().getConfianca() + "\n"
                + "Daniel: " + estado.getDaniel().getConfianca() + "\n"
                + "Helena: " + estado.getHelena().getConfianca() + "\n";

        informacoes = informacoes + "\nInventário\n";
        List<Integer> itens = protagonista.getInventario().getIdsDosItensDisponiveis();

        if (itens.isEmpty()) {
            informacoes = informacoes + "Nenhum item disponível.";
        } else {
            for (Integer id : itens) {
                Item item = protagonista.getInventario().itemPorId(id);

                if (item != null) {
                    informacoes = informacoes + "• " + item.getNome() + "\n";
                }
            }
        }

        informacoes = informacoes + "\nDecisões: " + estado.getDecisoes().size();

        Alert status = new Alert(Alert.AlertType.INFORMATION, informacoes, ButtonType.OK);
        status.setTitle("Status da partida");
        status.setHeaderText(estado.getAto().getRotulo());
        status.getDialogPane().setPrefWidth(460);
        status.showAndWait();
    }

    @FXML
    private void aoMenu() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja encerrar esta partida? O último salvamento automático foi feito no início de um ato. "
                + "As decisões posteriores a esse ponto poderão ser perdidas.", ButtonType.YES, ButtonType.NO);
        confirmacao.setHeaderText("Voltar ao menu principal");

        Optional<ButtonType> resposta = confirmacao.showAndWait();

        if (resposta.isPresent() && resposta.get() == ButtonType.YES) {
            interrompida = true;

            // libera a história, que estava esperando um clique; ela vai lançar PartidaInterrompidaException
            if (esperando) {
                esperando = false;
                Platform.exitNestedEventLoop(chaveDeEspera, null);
            }
        }
    }

    /**
     * Carrega opcionalmente /images/cenas/{id}_01.png, {id}_02.png ... ou {id}.png.
     * Sem arte, mostra um marcador discreto; cenas JSON não precisam ser alteradas.
     */
    private void atualizarArte(int indice) {
        if (cenaAtual == null || cenaAtual.isBlank()) return;
        String identificador = cenaAtual.replaceAll("[^A-Za-z0-9_-]", "_");
        URL caminho = null;

        // 1º: o catálogo diz qual arquivo usar neste diálogo
        String arquivo = CatalogoDeImagens.arquivoDe(identificador, indice);
        if (arquivo != null) {
            caminho = getClass().getResource("/images/cenas/" + arquivo + ".png");
        }

        // 2º: arquivo com o nome do diálogo (idDaCena_NN.png); 3º: arquivo com o nome da cena (idDaCena.png)
        if (caminho == null && indice > 0) {
            caminho = getClass().getResource(String.format("/images/cenas/%s_%02d.png", identificador, indice));
        }
        if (caminho == null) caminho = getClass().getResource("/images/cenas/" + identificador + ".png");
        if (caminho == null) {
            imagemCena.setImage(null);
            imagemCena.setVisible(false);
            rotuloImagemPendente.setVisible(true);
            rotuloImagemPendente.setText("SANTA AURORA  ·  " + identificador.replace('_', ' ').toUpperCase());
            return;
        }
        try {
            // carrega na hora (sem segundo plano) para a imagem aparecer junto com o texto;
            // 1964 px é o dobro da largura da caixa, suficiente para telas de alta densidade
            imagemCena.setImage(new Image(caminho.toExternalForm(), 1964, 0, true, true));
            imagemCena.setVisible(true);
            rotuloImagemPendente.setVisible(false);
        } catch (IllegalArgumentException e) {
            imagemCena.setImage(null);
            imagemCena.setVisible(false);
            rotuloImagemPendente.setVisible(true);
            rotuloImagemPendente.setText("Arte indisponível — " + identificador);
        }
    }
}
