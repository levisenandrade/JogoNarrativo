/**
 * classe que controla o fluxo da execução do jogo
 *
 * faz a ponte entre o jogador (por meio do EventBus), o estado da partida (EstadoPartida) e a persistência
 * (RepositorioDeSaves): inicia/continua/carrega partidas, executa as cenas de cada ato e
 * oferece os pontos de salvamento. As regras do jogo (requisitos, efeitos) ficam no Model.
 */

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Controlador {

    // valor devolvido por executaCena quando uma escolha com efeito GAME_OVER é aplicada
    private static final int SENTINELA_GAME_OVER = -1;

    // na lista de saves do menu, a opção 4 representa o salvamento automático (slot 0)
    private static final int OPCAO_DO_SAVE_AUTOMATICO = RepositorioDeSaves.ULTIMO_SLOT_MANUAL + 1;

    private final RepositorioDeSaves repositorio;
    private final RepositorioDeProgresso repositorioDeProgresso;
    private final RepositorioDeConfiguracoes repositorioDeConfiguracoes;

    private final EventBus eventBus;

    // última escolha publicada pela View (EventoEscolhaSelecionada)
    private Escolha escolhaRecebida;

    // preferências do jogador: valem para o jogo todo e são mantidas entre as execuções
    private Configuracoes configuracoes;

    // finais alcançados e conquistas: valem para o jogo todo, não para uma partida só
    private ProgressoGlobal progresso;

    /**
     * o Controlador não cria suas dependências: quem monta as peças do jogo é a classe Partida
     * (e os testes), o que também permite trocar a View ou o local dos saves sem mexer aqui
     *
     * @param eventBus canal usado para falar com o jogador: o Controlador publica eventos e a View reage
     * @param repositorio local de armazenamento dos saves
     * @param repositorioDeProgresso local de armazenamento dos finais alcançados e das conquistas
     * @param repositorioDeConfiguracoes local de armazenamento das preferências do jogador
     */
    public Controlador(EventBus eventBus, RepositorioDeSaves repositorio, RepositorioDeProgresso repositorioDeProgresso,
                       RepositorioDeConfiguracoes repositorioDeConfiguracoes) {
        this.eventBus = eventBus;
        this.repositorio = repositorio;
        this.repositorioDeProgresso = repositorioDeProgresso;
        this.repositorioDeConfiguracoes = repositorioDeConfiguracoes;
        this.progresso = new ProgressoGlobal();
        this.configuracoes = new Configuracoes();
        this.escolhaRecebida = null;
    }

    // ------------------------------------------------ comunicação com a View (EventBus)

    /**
     * liga ao EventBus o que o Controlador escuta; chamar uma vez, na montagem do jogo
     */
    public void registrarListeners() {
        eventBus.subscribe(EventoEscolhaSelecionada.class, new EscolhaSelecionadaListener(this));
    }

    /**
     * chamado pelo EscolhaSelecionadaListener: guarda a escolha que a View publicou
     * (null quando o diálogo não tinha opções). A decisão do que fazer com ela
     * continua em executaCena.
     */
    public void processarEscolha(Escolha escolha, Dialogo origem) {
        this.escolhaRecebida = escolha;
    }

    private void exibirTexto(String texto) {
        eventBus.publish(new EventoTextoDisponivel(texto));
    }

    private boolean confirmar(String pergunta) {
        return solicitar(new EventoConfirmacaoSolicitada(pergunta));
    }

    /**
     * publica um pedido e devolve a resposta da View
     *
     * Só funciona quando o Controlador roda fora de um listener (o EventBus entrega na hora).
     * Dentro de um listener o pedido ficaria na fila e a resposta ainda não existiria.
     */
    private <R> R solicitar(EventoRequisicao<R> pedido) {
        eventBus.publish(pedido);

        if (!pedido.foiRespondido()) {
            throw new IllegalStateException("Nenhuma View respondeu ao pedido publicado no EventBus.");
        }

        return pedido.getResposta();
    }

    /**
     * publica um evento que a View responde com EventoEscolhaSelecionada e devolve essa escolha
     */
    private Escolha pedirEscolha(Evento pedido) {
        escolhaRecebida = null;
        eventBus.publish(pedido);

        if (escolhaRecebida == null) {
            throw new IllegalStateException("A View não publicou nenhuma escolha.");
        }

        return escolhaRecebida;
    }

    /**
     * prepara o jogo para uso: lê as configurações e o progresso gravados em disco
     *
     * é chamado pelo menuInicial() (versão de terminal) e também pela App (versão JavaFX),
     * que não usa o loop do menuInicial()
     */
    public void inicializar() {
        carregarConfiguracoes();
        carregarProgresso();
    }

    /**
     * executa a ação correspondente a uma opção do menu principal
     *
     * é usado pelo menuInicial() (versão de terminal, com o número digitado) e pode ser usado
     * pela versão gráfica, em que cada botão chama este método com o número da sua opção
     *
     * @param opcao número da opção (1 a 8, o mesmo mostrado no menu principal)
     */
    public void executarOpcaoDoMenu(int opcao) {

        switch (opcao) {

            case 1:

                break;

            case 2:
                continuarPartida();
                break;

            case 3:
                gerenciarSaves();
                break;

            case 4:
                mostrarConquistas();
                break;

            case 5:
                gerenciarConfiguracoes();
                break;

            case 6:
                eventBus.publish(new EventoTelaSolicitada(EventoTelaSolicitada.Tela.INSTRUCOES));
                break;

            case 7:
                eventBus.publish(new EventoTelaSolicitada(EventoTelaSolicitada.Tela.CREDITOS));
                break;

            case 8:
                eventBus.publish(new EventoTelaSolicitada(EventoTelaSolicitada.Tela.SAIDA));
                break;

        }
    }

    /**
     * obtém o protagonista da nova partida
     *
     * Primeiro oferece o personagem favorito, se existir. Se o jogador recusar (ou não houver favorito),
     * cadastra um personagem novo com as informações fornecidas e oferece guardá-lo como favorito.
     * Em todos os casos faz o cadastro dos itens disponíveis para o inventário do protagonista.
     *
     * @return o protagonista criado ou escolhido para a partida
     */
    public Protagonista criarProtagonista(String nome,int idade, String genero) {
        Protagonista favorito = perguntaUsarFavorito();

        if (favorito != null) {
            return favorito;
        }

        Protagonista protagonista = new Protagonista(nome, idade, genero);
        protagonista.cadastrarItens();

        perguntaSalvarFavorito(protagonista);

        return protagonista;
    }

    /**
     * pergunta se o jogador quer usar o personagem favorito nesta partida
     *
     * @return o protagonista criado a partir do favorito, ou null se não há favorito ou o jogador recusou
     */
    private Protagonista perguntaUsarFavorito() {
        PersonagemFavorito favorito = configuracoes.getPersonagemFavorito();

        if (favorito == null) {
            return null;
        }

        String descricao = favorito.getNome() + ", " + favorito.getIdade() + " anos, " + favorito.getGenero();

        if (!confirmar("Deseja usar seu personagem favorito (" + descricao + ") nesta nova partida?")) {
            return null;
        }

        Protagonista protagonista = new Protagonista(favorito.getNome(), favorito.getIdade(), favorito.getGenero());
        protagonista.cadastrarItens();
        return protagonista;
    }

    /**
     * pergunta se o jogador quer guardar o personagem recém-criado como favorito
     *
     * Se já existir um favorito, pede confirmação antes de substituí-lo. Uma falha ao gravar
     * só avisa o jogador e não interrompe a partida.
     *
     * @param protagonista personagem recém-criado
     */
    private void perguntaSalvarFavorito(Protagonista protagonista) {
        if (!confirmar("Deseja salvar este personagem como favorito?")) {
            return;
        }

        if (configuracoes.temPersonagemFavorito()
                && !confirmar("Já existe um favorito salvo. Deseja substituí-lo?")) {
            return;
        }

        configuracoes.setPersonagemFavorito(new PersonagemFavorito(
                protagonista.getNome(), protagonista.getIdade(), protagonista.getGenero()));

        if (salvarConfiguracoes()) {
            exibirTexto("Personagem salvo como favorito.");
        }
    }

    // ------------------------------------------------------------ configurações

    /**
     * lê as configurações gravadas em disco; se não for possível, o jogo avisa e usa os valores padrão
     */
    private void carregarConfiguracoes() {
        try {
            configuracoes = repositorioDeConfiguracoes.carregar();
        } catch (CarregamentoException e) {
            exibirTexto("Aviso: não foi possível carregar suas configurações (" + e.getMessage()
                    + "). Serão usados os valores padrão.");
            configuracoes = new Configuracoes();
        }

        eventBus.publish(new EventoConfiguracoesAtualizadas(configuracoes));
    }

    /**
     * grava as configurações; se falhar, só avisa o jogador
     *
     * @return true se gravou, false se não foi possível gravar
     */
    private boolean salvarConfiguracoes() {
        try {
            repositorioDeConfiguracoes.salvar(configuracoes);
            return true;
        } catch (SaveException e) {
            exibirTexto("Aviso: não foi possível salvar as configurações. " + e.getMessage());
            return false;
        }
    }

    /**
     * tela de configurações do menu principal: liga/desliga as opções e gerencia o personagem favorito
     */
    private void gerenciarConfiguracoes() {
        int opcao;

        do {
            opcao = solicitar(new EventoConfiguracoesSolicitadas(configuracoes));

            if (opcao == 1) {
                configuracoes.setMostrarEfeitosDasEscolhas(!configuracoes.isMostrarEfeitosDasEscolhas());
                salvarConfiguracoes();
            } else if (opcao == 2) {
                configuracoes.setPausaEntreDialogos(!configuracoes.isPausaEntreDialogos());
                eventBus.publish(new EventoConfiguracoesAtualizadas(configuracoes));
                salvarConfiguracoes();
            } else if (opcao == 3) {
                gerenciarFavorito();
            }
        } while (opcao != 0);
    }

    private void gerenciarFavorito() {
        if (!configuracoes.temPersonagemFavorito()) {
            exibirTexto("Você ainda não tem um personagem favorito. Crie um personagem em uma nova partida "
                    + "e escolha salvá-lo como favorito.");
            return;
        }

        String nome = configuracoes.getPersonagemFavorito().getNome();

        if (confirmar("Deseja excluir o personagem favorito " + nome + "?")) {
            configuracoes.setPersonagemFavorito(null);

            if (salvarConfiguracoes()) {
                exibirTexto("Personagem favorito excluído.");
            }
        }
    }


    // ------------------------------------------------- conquistas e registro de finais

    /**
     * lê o progresso gravado em disco; se não for possível, o jogo avisa e segue com o progresso vazio
     */
    private void carregarProgresso() {
        try {
            progresso = repositorioDeProgresso.carregar();
        } catch (CarregamentoException e) {
            exibirTexto("Aviso: não foi possível carregar suas conquistas (" + e.getMessage()
                    + "). O progresso começará vazio.");
            progresso = new ProgressoGlobal();
        }
    }

    private void salvarProgresso() {
        try {
            repositorioDeProgresso.salvar(progresso);
        } catch (SaveException e) {
            exibirTexto("Aviso: não foi possível salvar o progresso. " + e.getMessage());
        }
    }

    /**
     * tela de conquistas do menu principal: mostra as conquistas e permite redefini-las
     */
    private void mostrarConquistas() {
        int opcao;

        do {
            opcao = solicitar(new EventoConquistasSolicitadas(progresso));

            if (opcao == 1) {
                redefinirConquistas();
            }
        } while (opcao != 0);
    }

    private void redefinirConquistas() {
        if (!confirmar("Deseja realmente apagar as conquistas e o registro de finais? Essa ação não pode ser desfeita.")) {
            return;
        }

        progresso = new ProgressoGlobal();
        salvarProgresso();
        exibirTexto("Conquistas redefinidas.");
    }

    /**
     * registra o final que o jogador acabou de alcançar, guarda o progresso e avisa das conquistas novas
     *
     * @param idDoFinal número do final (o mesmo de EstadoPartida.getFinalEscolhido)
     */
    private void registrarFinalAlcancado(int idDoFinal) {
        FinalDoJogo alcancado = FinalDoJogo.porId(idDoFinal);

        if (alcancado == null) {
            return;
        }

        List<Conquista> novas = progresso.registrarFinal(alcancado);

        if (!novas.isEmpty()) {
            salvarProgresso();
        }

        for (int i = 0; i < novas.size(); i++) {
            eventBus.publish(new EventoConquistaDesbloqueada(novas.get(i)));
        }
    }

    // ------------------------------------------------------------------ saves

    /**
     * retoma a partida do save mais recente (entre os slots manuais e o automático)
     */
    private void continuarPartida() {
        int slot = slotMaisRecente();

        if (slot == -1) {
            exibirTexto("Não há partidas salvas para continuar.");
            return;
        }

        carregarEExecutar(slot);
    }

    /**
     * tela de saves do menu principal: lista os slots e permite carregar ou excluir
     */
    private void gerenciarSaves() {
        while (true) {
            int opcao = solicitar(new EventoListaDeSavesSolicitada("CARREGAR PARTIDA", descricaoDosSlots(true)));

            if (opcao == 0) {
                return;
            }

            int slot = (opcao == OPCAO_DO_SAVE_AUTOMATICO) ? RepositorioDeSaves.SLOT_AUTOMATICO : opcao;

            if (!repositorio.existe(slot)) {
                exibirTexto("Esse slot está vazio.");
                continue;
            }

            int acao = solicitar(new EventoAcaoDoSaveSolicitada());

            if (acao == 1) {
                carregarEExecutar(slot);
                return;
            }

            if (acao == 2) {
                excluirComConfirmacao(slot);
            }
        }
    }

    private void carregarEExecutar(int slot) {
        EstadoPartida estado;

        try {
            estado = repositorio.carregar(slot);
        } catch (CarregamentoException e) {
            exibirTexto("Não foi possível carregar a partida: " + e.getMessage());
            return;
        }

        exibirTexto("Partida carregada.");
        executarHistoria(estado);
    }

    private void excluirComConfirmacao(int slot) {
        if (!confirmar("Deseja realmente excluir esse save? Essa ação não pode ser desfeita.")) {
            return;
        }

        try {
            repositorio.excluir(slot);
            exibirTexto("Save excluído.");
        } catch (SaveException e) {
            exibirTexto("Não foi possível excluir o save: " + e.getMessage());
        }
    }

    /**
     * @return o slot (0 a 3) do save mais recente, ou -1 se não houver nenhum save válido
     */
    private int slotMaisRecente() {
        int melhorSlot = -1;
        LocalDateTime melhorData = null;

        for (int slot = RepositorioDeSaves.SLOT_AUTOMATICO; slot <= RepositorioDeSaves.ULTIMO_SLOT_MANUAL; slot++) {
            try {
                MetadadosSave metadados = repositorio.lerMetadados(slot);

                if (metadados != null) {
                    LocalDateTime data = metadados.getDataHora();

                    if (melhorData == null || data.isAfter(melhorData)) {
                        melhorData = data;
                        melhorSlot = slot;
                    }
                }
            } catch (CarregamentoException e) {
                // save corrompido não entra na disputa pelo mais recente
            }
        }

        return melhorSlot;
    }

    /**
     * monta a descrição de cada slot para as telas de saves
     *
     * @param incluiAutomatico true para acrescentar o salvamento automático como última opção
     * @return uma linha por slot: slots manuais 1 a 3 e, se pedido, o automático
     */
    private List<String> descricaoDosSlots(boolean incluiAutomatico) {
        List<String> linhas = new ArrayList<>();

        for (int slot = RepositorioDeSaves.PRIMEIRO_SLOT_MANUAL; slot <= RepositorioDeSaves.ULTIMO_SLOT_MANUAL; slot++) {
            linhas.add(descricaoDoSlot(slot));
        }

        if (incluiAutomatico) {
            linhas.add("[Automático] " + descricaoDoSlot(RepositorioDeSaves.SLOT_AUTOMATICO));
        }

        return linhas;
    }

    private String descricaoDoSlot(int slot) {
        try {
            MetadadosSave metadados = repositorio.lerMetadados(slot);

            return (metadados == null) ? "(vazio)" : metadados.descricao();
        } catch (CarregamentoException e) {
            return "(save corrompido)";
        }
    }

    private void salvarAutomaticamente(EstadoPartida estado) {
        try {
            repositorio.salvar(RepositorioDeSaves.SLOT_AUTOMATICO, estado);
            exibirTexto("[Progresso salvo automaticamente]");
        } catch (SaveException e) {
            exibirTexto("Aviso: não foi possível salvar automaticamente. " + e.getMessage());
        }
    }

    private void salvarManualmente(EstadoPartida estado) {
        int slot = solicitar(new EventoListaDeSavesSolicitada("SALVAR PARTIDA", descricaoDosSlots(false)));

        if (slot == 0) {
            return;
        }

        if (repositorio.existe(slot)
                && !confirmar("O slot " + slot + " já possui um save. Deseja sobrescrever?")) {
            return;
        }

        try {
            repositorio.salvar(slot, estado);
            exibirTexto("Partida salva no slot " + slot + ".");
        } catch (SaveException e) {
            exibirTexto("Não foi possível salvar a partida: " + e.getMessage());
        }
    }

    /**
     * ponto de salvamento: salva automaticamente e oferece o salvamento manual
     *
     * @return true se o jogador quer continuar a história, false se voltou ao menu principal
     */
    private boolean pontoDeSalvamento(EstadoPartida estado) {
        salvarAutomaticamente(estado);

        while (true) {
            int opcao = solicitar(new EventoPontoDeSalvamentoSolicitado(estado.getAto().getRotulo()));

            if (opcao == 1) {
                return true;
            }

            if (opcao == 2) {
                salvarManualmente(estado);
            } else if (confirmar("Voltar ao menu principal? Você poderá retomar daqui pelo salvamento automático.")) {
                return false;
            }
        }
    }

    // ---------------------------------------------------------------- história

    /**
     * executa a história a partir do ato em que o estado está (nova partida ou save carregado)
     *
     * se alguma cena não puder ser carregada (arquivo JSON ausente ou inválido), mostra o erro e
     * volta ao menu principal
     */
    public void executarHistoria(EstadoPartida estado) {
        try {
            rodarHistoria(estado);
        } catch (CenaInvalidaException e) {
            exibirTexto("Erro ao carregar a história: " + e.getMessage());
        }
    }

    /**
     * em cada ato, entrega as cenas, executa e decide o próximo ato de acordo com as escolhas
     */
    public void rodarHistoria(EstadoPartida estado) throws CenaInvalidaException {
        ConstrutorDeCenas construtor = new ConstrutorDeCenas(estado);

        while (estado.getAto() != Ato.FIM) {

            if (estado.getAto().ehPontoDeSalvamento() && !pontoDeSalvamento(estado)) {
                return; // voltou ao menu principal
            }

            switch (estado.getAto()) {

                case PROLOGO: {
                    executaCena(construtor.criarPrologo(), estado); // sem escolhas relevantes para roteamento
                    estado.setAto(Ato.ATO_I);
                    break;
                }

                case ATO_I: {
                    executaCena(construtor.criarAtoI(), estado);
                    estado.setAto(Ato.FINAL_ATO_I);
                    break;
                }

                case FINAL_ATO_I: {
                    int escolha = executaCena(construtor.criarFinalAtoI(), estado);
                    estado.setRotaAtoII(escolha); // 1..6, mapeado 1:1 com as Escolhas do desfecho do Ato I
                    estado.setAto(Ato.ATO_II);
                    break;
                }

                case ATO_II: {
                    Cena cena = obterCenaDoAtoII(estado.getRotaAtoII(), construtor);
                    if (cena == null) { estado.setAto(Ato.FIM); break; }

                    int escolha = executaCena(cena, estado);

                    // Rotas 2B e 3B terminam em Game Over dentro do próprio arquivo
                    // (não têm Escolha nenhuma — só narração de captura/prisão).
                    if (estado.getRotaAtoII() == 4 || estado.getRotaAtoII() == 6 || escolha == SENTINELA_GAME_OVER) {
                        estado.setAto(Ato.FIM);
                    } else {
                        estado.setAto(Ato.ATO_III);
                    }
                    break;
                }

                case ATO_III: {
                    Cena cena = obterCenaDoAtoIII(estado.getRotaAtoII(), construtor);
                    if (cena == null) { estado.setAto(Ato.FIM); break; }

                    int escolha = executaCena(cena, estado);

                    // O ato IV é o ponto que centraliza toda a história
                    estado.setAto(escolha == SENTINELA_GAME_OVER ? Ato.FIM : Ato.ATO_IV);
                    break;
                }

                case ATO_IV: {
                    int escolha = executaCena(construtor.criarAtoIV(), estado);
                    // escolha reflete a Escolha Decisiva final (Cena 4 do Ato IV):
                    // 1=Razão->Rota1A, 2=Paranoia->Rota1B, 3=Violência->Rota1C, 4/sentinela=Game Over
                    if (escolha >= 1 && escolha <= 3) {
                        estado.setRotaAtoV(escolha);
                        estado.setAto(Ato.ATO_V);
                    } else {
                        estado.setAto(Ato.FIM); // opção 4 (discurso sem provas) ou GameOver de outra etapa da cena
                    }
                    break;
                }

                case ATO_V: {
                    Cena cena;
                    switch (estado.getRotaAtoV()) {
                        case 1: cena = construtor.criarAtoVRota1A(); break;
                        case 2: cena = construtor.criarAtoVRota1B(); break;
                        case 3: cena = construtor.criarAtoVRota1C(); break;
                        default: cena = null;
                    }
                    if (cena == null) { estado.setAto(Ato.FIM); break; }

                    int escolha = executaCena(cena, estado);

                    estado.setAto(escolha == SENTINELA_GAME_OVER ? Ato.FIM : Ato.ATO_VI);
                    break;
                }

                case ATO_VI: {
                    int escolha = executaCena(construtor.criarAtoVI(), estado);
                    // 1=Balança, 2=Névoa, 3=Sangue, 4/sentinela=Game Over ("O Silêncio de Santa Aurora")
                    if (escolha >= 1 && escolha <= 3) {
                        estado.setFinalEscolhido(escolha);
                        estado.setAto(Ato.FINAL);
                    } else {
                        estado.setAto(Ato.FIM);
                    }
                    break;
                }

                case FINAL: {
                    Cena cena;
                    switch (estado.getFinalEscolhido()) {
                        case 1: cena = construtor.criarFinalBalanca(); break;
                        case 2: cena = construtor.criarFinalNevoa(); break;
                        case 3: cena = construtor.criarFinalSangue(); break;
                        default: cena = null;
                    }
                    if (cena != null) {
                        executaCena(cena, estado); // epílogo puro, sem escolhas
                        registrarFinalAlcancado(estado.getFinalEscolhido());
                    }
                    estado.setAto(Ato.FIM);
                    break;
                }

                case FIM:
                    break;
            }
        }
    }

    private Cena obterCenaDoAtoII(int rota, ConstrutorDeCenas construtor) throws CenaInvalidaException {
        switch (rota) {
            case 1: return construtor.criarAtoIIRota1A();
            case 2: return construtor.criarAtoIIRota1B();
            case 3: return construtor.criarAtoIIRota2A();
            case 4: return construtor.criarAtoIIRota2B();
            case 5: return construtor.criarAtoIIRota3A();
            case 6: return construtor.criarAtoIIRota3B();
            default: return null;
        }
    }

    private Cena obterCenaDoAtoIII(int rotaAtoII, ConstrutorDeCenas construtor) throws CenaInvalidaException {
        switch (rotaAtoII) {
            case 1: return construtor.criarAtoIIIPerspectiva1A();
            case 2: return construtor.criarAtoIIIPerspectiva1B();
            case 3: return construtor.criarAtoIIIPerspectiva2A();
            case 5: return construtor.criarAtoIIIPerspectiva3A();
            default: return null; // 4 e 6 já terminaram no Ato II
        }
    }

    /**
     * controla a progressão da narrativa dentro de uma cena.
     *
     * mostra cada diálogo, pede a escolha do jogador, impede escolhas bloqueadas (a regra de
     * bloqueio fica no EstadoPartida), aplica os efeitos e registra a decisão tomada.
     *
     * @return o número da última escolha feita na cena (usado para decidir a próxima rota),
     *         0 se a cena não teve escolhas ou SENTINELA_GAME_OVER se uma escolha encerrou a partida
     */
    private int executaCena(Cena cena, EstadoPartida estado) {
        if (cena == null || cena.getDialogos() == null) {
            return 0;
        }

        int ultimaEscolha = 0;
        List<Dialogo> dialogos = cena.getDialogos();

        for (int i = 0; i < dialogos.size(); i++) {
            Dialogo dialogo = dialogos.get(i);

            if (dialogo == null) {
                continue;
            }

            if (!dialogo.possuiOpcoes()) {
                // sem opções: a View apenas exibe o diálogo
                eventBus.publish(new EventoDialogoDisponivel(dialogo));
            } else {
                // com opções: a View exibe e responde com EventoEscolhaSelecionada
                Escolha escolhaSelecionada = pedirEscolha(new EventoDialogoDisponivel(dialogo));
                String motivo = estado.motivoDeBloqueio(escolhaSelecionada);

                while (motivo != null) {
                    // a View mostra o motivo e pede uma nova escolha
                    escolhaSelecionada = pedirEscolha(new EventoEscolhaBloqueada(dialogo, motivo));
                    motivo = estado.motivoDeBloqueio(escolhaSelecionada);
                }

                // número da escolha, o mesmo mostrado ao jogador (começa em 1)
                int escolha = dialogo.getListaEscolhas().indexOf(escolhaSelecionada) + 1;

                if (escolhaSelecionada.getTextoConsequencia() != null) {
                    exibirTexto(escolhaSelecionada.getTextoConsequencia());
                }

                estado.registrarDecisao(estado.getAto().name() + "#" + (i + 1) + "=" + escolha);

                // se a preferência estiver ligada, guarda os valores de antes para mostrar o que mudou
                boolean mostrarEfeitos = configuracoes.isMostrarEfeitosDasEscolhas();
                int[] atributosAntes = null;
                int[] confiancasAntes = null;

                if (mostrarEfeitos) {
                    atributosAntes = fotografarAtributos(estado);
                    confiancasAntes = fotografarConfiancas(estado);
                }

                boolean gameOver = estado.aplicarEfeitos(escolhaSelecionada);

                if (mostrarEfeitos) {
                    mostrarResultadoDaEscolha(atributosAntes, confiancasAntes, estado);
                }

                // game over: a cena é interrompida na hora e o jogo volta para o menu
                if (gameOver) {
                    return SENTINELA_GAME_OVER;
                }

                ultimaEscolha = escolha; // guarda a última escolha feita
            }
        }

        return ultimaEscolha;
    }



    // ------------------------------------------- mostrar os efeitos das escolhas

    /**
     * copia o valor de cada atributo do protagonista naquele momento (na ordem de NOMES_DOS_ATRIBUTOS)
     */
    private int[] fotografarAtributos(EstadoPartida estado) {
        String[] nomes = Protagonista.NOMES_DOS_ATRIBUTOS;
        int[] valores = new int[nomes.length];

        for (int i = 0; i < nomes.length; i++) {
            valores[i] = estado.getProtagonista().getAtributo(nomes[i]);
        }

        return valores;
    }

    /**
     * copia a confiança dos NPCs naquele momento, na ordem Jonas, Daniel, Helena
     */
    private int[] fotografarConfiancas(EstadoPartida estado) {
        int[] valores = new int[3];

        valores[0] = estado.getJonas().getConfianca();
        valores[1] = estado.getDaniel().getConfianca();
        valores[2] = estado.getHelena().getConfianca();

        return valores;
    }

    /**
     * mostra ao jogador o que mudou depois da escolha, comparando os valores de antes com os de agora
     *
     * @param atributosAntes valores dos atributos antes de aplicar os efeitos
     * @param confiancasAntes confiança de Jonas, Daniel e Helena antes de aplicar os efeitos
     * @param estado estado da partida, já com os efeitos aplicados
     */
    private void mostrarResultadoDaEscolha(int[] atributosAntes, int[] confiancasAntes, EstadoPartida estado) {
        String[] nomes = Protagonista.NOMES_DOS_ATRIBUTOS;

        for (int i = 0; i < nomes.length; i++) {
            mostrarVariacao(nomes[i], atributosAntes[i], estado.getProtagonista().getAtributo(nomes[i]));
        }

        mostrarVariacao("Relação com Jonas", confiancasAntes[0], estado.getJonas().getConfianca());
        mostrarVariacao("Relação com Daniel", confiancasAntes[1], estado.getDaniel().getConfianca());
        mostrarVariacao("Relação com Helena", confiancasAntes[2], estado.getHelena().getConfianca());
    }

    /**
     * mostra uma linha como [Razão +3] ou [Relação com Jonas -10], só se o valor mudou
     */
    private void mostrarVariacao(String nome, int antes, int depois) {
        int variacao = depois - antes;

        if (variacao > 0) {
            exibirTexto("[" + nome + " +" + variacao + "]");
        } else if (variacao < 0) {
            exibirTexto("[" + nome + " " + variacao + "]");
        }
    }
}