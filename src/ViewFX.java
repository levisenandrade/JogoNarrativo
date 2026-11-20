import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A View gráfica (equivalente do Menu do terminal). Padrão Observer: ela se inscreve uma única vez
 * no EventBus e reage aos eventos que o Controlador publica. Não decide nada da narrativa:
 * só mostra o que recebeu e devolve a resposta do jogador.
 *
 * Diálogos e pontos de salvamento aparecem na tela do jogo (JogoController);
 * confirmações, avisos, listas de saves, configurações e conquistas aparecem em janelas de diálogo.
 */
public class ViewFX {

    private final EventBus eventBus;
    private final NavegadorDeTelas navegador;
    private JogoController jogoAtual; // null quando a tela do jogo não está aberta

    public ViewFX(EventBus eventBus, NavegadorDeTelas navegador) {
        this.eventBus = eventBus;
        this.navegador = navegador;
    }

    public void registrarNoBus() {
        eventBus.subscribe(EventoPartidaIniciada.class,             this::aoPartidaIniciada);
        eventBus.subscribe(EventoPartidaEncerrada.class,            this::aoPartidaEncerrada);
        eventBus.subscribe(EventoCenaIniciada.class,                this::aoCenaIniciada);
        eventBus.subscribe(EventoDialogoDisponivel.class,           this::aoDialogoDisponivel);
        eventBus.subscribe(EventoEscolhaBloqueada.class,            this::aoEscolhaBloqueada);
        eventBus.subscribe(EventoTextoDisponivel.class,             this::aoTextoDisponivel);
        eventBus.subscribe(EventoConfirmacaoSolicitada.class,       this::aoConfirmacaoSolicitada);
        eventBus.subscribe(EventoPontoDeSalvamentoSolicitado.class, this::aoPontoDeSalvamentoSolicitado);
        eventBus.subscribe(EventoListaDeSavesSolicitada.class,      this::aoListaDeSavesSolicitada);
        eventBus.subscribe(EventoAcaoDoSaveSolicitada.class,        this::aoAcaoDoSaveSolicitada);
        eventBus.subscribe(EventoConfiguracoesSolicitadas.class,    this::aoConfiguracoesSolicitadas);
        eventBus.subscribe(EventoConquistasSolicitadas.class,       this::aoConquistasSolicitadas);
        eventBus.subscribe(EventoConquistaDesbloqueada.class,       this::aoConquistaDesbloqueada);
    }

    /** a tela do jogo; se ela não estiver aberta, a história não tem mais onde continuar */
    private JogoController jogoAberto() {
        if (jogoAtual == null) {
            throw new PartidaInterrompidaException();
        }
        return jogoAtual;
    }

    // ---------------------------------------------------------------- partida

    private void aoPartidaIniciada(EventoPartidaIniciada evento) {
        jogoAtual = navegador.mostrarJogo();

        if (jogoAtual != null) {
            jogoAtual.definirEstado(evento.getEstado());
        }
    }

    private void aoPartidaEncerrada(EventoPartidaEncerrada evento) {
        if (jogoAtual != null) {
            jogoAtual = null;
            navegador.mostrarMenu();
        }
    }

    private void aoCenaIniciada(EventoCenaIniciada evento) {
        jogoAberto().definirCena(evento.getCena().getCenaId());
    }

    // ---------------------------------------------------------------- narrativa

    private void aoDialogoDisponivel(EventoDialogoDisponivel evento) {
        Dialogo dialogo = evento.getDialogo();
        int opcao = jogoAberto().mostrarDialogo(dialogo, null, evento.getIndice()); // 0 se não há opções
        eventBus.publish(new EventoEscolhaSelecionada(dialogo.getEscolha(opcao), dialogo));
    }

    private void aoEscolhaBloqueada(EventoEscolhaBloqueada evento) {
        Dialogo dialogo = evento.getDialogo();
        int opcao = jogoAberto().mostrarDialogo(dialogo, evento.getMotivo(), 0);
        eventBus.publish(new EventoEscolhaSelecionada(dialogo.getEscolha(opcao), dialogo));
    }

    private void aoTextoDisponivel(EventoTextoDisponivel evento) {
        if (jogoAtual != null) {
            jogoAtual.mostrarAviso(evento.getTexto());
        } else {
            mostrarInformacao(evento.getTexto());
        }
    }

    private void aoPontoDeSalvamentoSolicitado(EventoPontoDeSalvamentoSolicitado evento) {
        evento.responder(jogoAberto().mostrarPontoDeSalvamento(evento.getRotuloDoAto()));
    }

    // ---------------------------------------------------------------- saves, configurações e conquistas

    private void aoConfirmacaoSolicitada(EventoConfirmacaoSolicitada evento) {
        evento.responder(confirmar(evento.getPergunta()));
    }

    private void aoListaDeSavesSolicitada(EventoListaDeSavesSolicitada evento) {
        evento.responder(escolherNaLista(evento.getTitulo(), evento.getLinhas()));
    }

    private void aoAcaoDoSaveSolicitada(EventoAcaoDoSaveSolicitada evento) {
        List<String> acoes = List.of("Carregar partida", "Excluir save");
        evento.responder(escolherNaLista("O que deseja fazer com esse save?", acoes));
    }

    private void aoConfiguracoesSolicitadas(EventoConfiguracoesSolicitadas evento) {
        Configuracoes conf = evento.getConfiguracoes();
        String favorito = conf.getPersonagemFavorito() == null ? "Nenhum" : conf.getPersonagemFavorito().getNome();

        List<String> linhas = List.of(
                "Mostrar efeitos das escolhas: " + simNao(conf.isMostrarEfeitosDasEscolhas()),
                "Pausa entre diálogos (modo terminal): " + simNao(conf.isPausaEntreDialogos()),
                "Excluir personagem favorito (atual: " + favorito + ")");

        evento.responder(escolherNaLista("CONFIGURAÇÕES", linhas));
    }

    private void aoConquistasSolicitadas(EventoConquistasSolicitadas evento) {
        ProgressoGlobal progresso = evento.getProgresso();

        String texto = "Finais descobertos: " + progresso.getQuantidadeDeFinais()
                + " de " + ProgressoGlobal.TOTAL_DE_FINAIS + "\n\n";

        for (Conquista conquista : Conquista.values()) {
            boolean desbloqueada = progresso.temConquista(conquista);
            boolean oculta = conquista.isOculta() && !desbloqueada;

            texto = texto + (desbloqueada ? "[CONCLUÍDA] " : "[BLOQUEADA] ")
                    + (oculta ? "????" : conquista.getTitulo()) + "\n"
                    + (oculta ? "Conquista oculta" : conquista.getDescricao()) + "\n";

            if (desbloqueada) {
                texto = texto + "Desbloqueada em: " + progresso.getDataDaConquista(conquista) + "\n";
            }

            texto = texto + "\n";
        }

        ButtonType redefinir = new ButtonType("Redefinir conquistas", ButtonBar.ButtonData.OTHER);
        ButtonType voltar = new ButtonType("Voltar", ButtonBar.ButtonData.CANCEL_CLOSE);

        Label descricao = new Label(texto);
        descricao.setWrapText(true);

        ScrollPane rolagem = new ScrollPane(descricao);
        rolagem.setFitToWidth(true);
        rolagem.setPrefViewportWidth(530);
        rolagem.setPrefViewportHeight(340);

        Alert alerta = new Alert(Alert.AlertType.NONE);
        alerta.setTitle("Conquistas — Santa Aurora");
        alerta.setHeaderText("Galeria de conquistas");
        alerta.getDialogPane().setContent(rolagem);
        alerta.getButtonTypes().setAll(redefinir, voltar);

        Optional<ButtonType> resposta = alerta.showAndWait();
        boolean querRedefinir = resposta.isPresent() && resposta.get() == redefinir;
        evento.responder(querRedefinir ? 1 : 0);
    }

    private void aoConquistaDesbloqueada(EventoConquistaDesbloqueada evento) {
        Conquista conquista = evento.getConquista();
        mostrarInformacao("Conquista desbloqueada: " + conquista.getTitulo() + "\n" + conquista.getDescricao());
    }

    // ---------------------------------------------------------------- janelas de diálogo

    private String simNao(boolean valor) {
        return valor ? "Ativado" : "Desativado";
    }

    private void mostrarInformacao(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION, mensagem, ButtonType.OK);
        alerta.setTitle("Santa Aurora");
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    /** pergunta Sim/Não ao jogador */
    private boolean confirmar(String pergunta) {
        ButtonType sim = new ButtonType("Sim", ButtonBar.ButtonData.YES);
        ButtonType nao = new ButtonType("Não", ButtonBar.ButtonData.NO);

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, pergunta, sim, nao);
        alerta.setTitle("Confirmação — Santa Aurora");
        alerta.setHeaderText(null);

        Optional<ButtonType> resposta = alerta.showAndWait();
        return resposta.isPresent() && resposta.get() == sim;
    }

    /**
     * mostra uma lista de opções (um botão por linha, mais "Voltar")
     *
     * @return o número da linha escolhida (1, 2, 3...) ou 0 para voltar
     */
    private int escolherNaLista(String titulo, List<String> linhas) {
        List<ButtonType> botoes = new ArrayList<>();

        for (int i = 0; i < linhas.size(); i++) {
            botoes.add(new ButtonType((i + 1) + " — " + linhas.get(i), ButtonBar.ButtonData.OTHER));
        }

        botoes.add(new ButtonType("Voltar", ButtonBar.ButtonData.CANCEL_CLOSE));

        Alert alerta = new Alert(Alert.AlertType.NONE);
        alerta.setTitle(titulo);
        alerta.setHeaderText(titulo);
        alerta.getButtonTypes().setAll(botoes);

        Optional<ButtonType> resposta = alerta.showAndWait();

        if (resposta.isPresent()) {
            int posicao = botoes.indexOf(resposta.get());

            if (posicao >= 0 && posicao < linhas.size()) {
                return posicao + 1;
            }
        }

        return 0; // Voltar ou janela fechada
    }
}
