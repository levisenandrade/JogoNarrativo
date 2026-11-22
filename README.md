# Jogo Narrativo Interativo em Java
EXA863 - MI PROGRAMAÇÃO

Componentes da dupla:
* Levi Sena (https://github.com/levisenandrade)
* Luis Felipe Batista (https://github.com/LuisB4tista)

Tutor: Roberto Almeida Bittencourt

Universidade Estadual de Feira de Santana (UEFS) — 2026.2

---

## 1. Introdução

Jogos narrativos interativos são aplicações em que o jogador conduz uma história por meio de escolhas, e em que cada decisão altera o rumo da trama, a relação com os personagens e o desfecho final. Por trás da experiência de leitura, existe um sistema que precisa controlar estado, regras de acesso, consequências e persistência de dados de forma organizada.

A história se passa em Santa Aurora, onde o jogador assume o papel de um protagonista envolvido em uma investigação marcada por mistérios, suspeitas e relações com diferentes personagens. Ao longo da trama, suas escolhas influenciam o desenvolvimento da investigação, a confiança dos envolvidos e os caminhos que podem levar a diferentes desfechos.

Este projeto teve como objetivo o desenvolvimento de um jogo narrativo interativo em linguagem Java, no estilo *visual novel*/RPG narrativo, seguindo os princípios da Programação Orientada a Objetos (POO) e o padrão arquitetural **MVC** (*Model-View-Controller*). Não foi utilizada nenhuma *engine* de jogos pronta: toda a lógica de narrativa, relacionamentos, atributos, recursos e finais foi implementada do zero.

O desenvolvimento foi dividido em três fases:

* **Fase 1:** jogo jogável pelo terminal, com menu, protagonista configurável, ao menos 10 cenas, escolhas com consequências, relacionamentos, atributos, recursos e 3 finais distintos;
* **Fase 2:** sistema de persistência em **JSON**, com múltiplos *slots* de save, salvamento manual e automático, sobrescrita, exclusão, configurações persistentes e exceções customizadas;
* **Fase 3:** interface gráfica com **JavaFX**, preservando o MVC e aplicando padrões de projeto GoF.

---

## 2. Especificação do Projeto

| Item | Valor |
|---|---|
| Linguagem | Java 17 ou superior |
| Interface (Fases 1 e 2) | Terminal (console) |
| Interface (Fase 3) | JavaFX |
| Persistência | Arquivos JSON - Biblioteca: Gson |
| Testes de unidade | JUnit 4/5 |
| Gerenciador de build | Maven |
| IDE de desenvolvimento | IntelliJ IDEA |
| Controle de versão | Git / GitHub |

---

## 3. Fundamentação Teórica

### 3.1. Programação Orientada a Objetos

O projeto foi modelado com classes que representam as entidades do domínio do jogo (protagonista, personagens não jogáveis, cenas, escolhas, estado da partida), aplicando os conceitos de encapsulamento, abstração, herança e polimorfismo vistos em sala. [PREENCHER: citar onde herança/polimorfismo/interfaces foram usados de fato no código.]

### 3.2. Padrão MVC

A separação em três camadas é o eixo da arquitetura:

* **Model:** guarda os dados e as regras do jogo (protagonista, NPCs, cenas, estado da partida), sem saber nada sobre como serão exibidos;
* **View:** responsável apenas pela exibição e pela captura de entradas do jogador (terminal na Fase 1/2, JavaFX na Fase 3);
* **Controller:** recebe as ações da View, aplica as regras sobre o Model e decide o que será exibido em seguida.

### 3.3. Persistência com JSON

JSON é um formato textual leve e legível, adequado para representar o estado de uma partida (campos simples, listas e objetos aninhados). Ele foi utilizado em dois pontos do projeto: na **definição das cenas** (carregadas por `CarregadorDeCenas`) e no **salvamento das partidas** e configurações do jogador. A escolha por JSON em vez de XML foi motivada pela simplicidade de leitura e escrita.


### 3.4. Padrões de projeto (GoF)

Foram aplicados três padrões GoF:

* **Observer** (`EventBus`, `EventListener`, `ViewFX`): o Controlador publica eventos (diálogo disponível, conquista desbloqueada, partida iniciada...) e a View se inscreve para reagir a eles. Assim o Controlador não conhece a View, e o mesmo Controlador funciona com o terminal (`Menu`) e com o JavaFX (`ViewFX`).
* **Facade** (`NavegadorDeTelas`): esconde dos controllers das telas os detalhes do JavaFX (`FXMLLoader`, `Scene`, `Stage`). Para trocar de tela, basta chamar `mostrarMenu()`, `mostrarCriacao()`, `mostrarJogo()` ou `abrirJanela(...)`.
* **Strategy** (`RepositorioDeSaves`, `RepositorioJson`, `RepositorioEmMemoria`): o Controlador depende só da interface `RepositorioDeSaves`; a forma de guardar os saves (arquivos JSON ou memória) pode ser trocada sem alterar o Controlador.

---


## 4. Arquitetura

### 4.1. Organização em pacotes

```
src/
├── model/         # entidades e regras do jogo
├── view/          # exibição e entrada do jogador
├── controller/    # fluxo do jogo e orquestração
├── persistencia/  # leitura/escrita de JSON, saves e configurações
├── excecoes/      # exceções customizadas
└── Main.java
test/              # testes de unidade
recursos/cenas/    # arquivos JSON com as cenas
```

> [AJUSTAR a árvore acima para refletir a estrutura real do repositório.]

### 4.2. Diagrama de classes

[INSERIR imagem ou link do diagrama de classes, ex.: `docs/diagrama-de-classes.png`]

### 4.3. Fluxo geral de uma partida

1. O jogador abre o **menu inicial** (Nova partida, Carregar, Instruções, Créditos, Conquistas, Configurações, Sair);
2. Em *Nova partida*, escolhe um personagem salvo (preferência de personagem favorito) ou cria um novo protagonista;
3. O jogo apresenta a **introdução narrativa** e avança pelas cenas;
4. A cada cena, o jogador escolhe entre as opções **liberadas** pelas condições de acesso (atributos, recursos, relacionamentos, decisões anteriores);
5. As escolhas aplicam **efeitos** sobre atributos, recursos e relacionamentos e atualizam o estado da partida;
6. Ao final, é exibido um dos finais disponíveis e o jogador retorna ao menu.

### 4.4. `EstadoPartida` — o estado central do jogo

A classe `EstadoPartida` concentra tudo o que define uma partida em andamento: o protagonista, os NPCs e seus relacionamentos, o ato atual, as rotas seguidas, as decisões tomadas, as regras de bloqueio/efeitos das escolhas e o resumo da jornada. Por reunir todo o estado em um único objeto, ela é o elemento que a Fase 2 serializa para JSON ao salvar e reconstrói ao carregar uma partida.

### 4.5. `CarregadorDeCenas` — cenas em JSON

As cenas do jogo ficam em arquivos JSON, e não dentro do código. O `CarregadorDeCenas` lê esses arquivos e monta os objetos de cena (texto, diálogos, escolhas, condições e efeitos). Com isso, o conteúdo narrativo pode ser alterado sem recompilar o projeto.

---

## 5. Detalhamento das Funcionalidades

## 5.1. Protagonista e atributos

O protagonista é configurado pelo jogador no início da partida (nome, idade e gênero) e possui **4 atributos variáveis**, todos iniciando em **0** e limitados a **100**:

| Atributo | Papel na história |
|---|---|
| Razão | Caminho da lei e das provas; leva ao final *A Balança* |
| Paranoia | Caminho da desconfiança e da revolta popular; leva ao final *A Névoa se Levanta* |
| Violência | Caminho da força e da vingança; leva ao final *O Preço do Sangue* |
| Provas | Evidências reunidas ao longo da investigação |

Os atributos evoluem conforme as escolhas (as opções de caráter costumam dar +3 no atributo correspondente, e as de investigação dão +1 em Provas) e podem ser exigidos como condição de acesso a certas opções (ver 5.4).

### 5.2. Personagens e relacionamentos

O jogo conta com 3 personagens relevantes (Daniel, Helena e Jonas). Os relacionamentos com eles são medidos por **confiança**, em escala de **0 a 100**, iniciando em **50** e subindo ou descendo conforme as ações do jogador.

### 5.3. Recursos gerenciáveis

O recurso gerenciável do jogo é o **inventário**, gerenciado pela classe `Inventario`. Ele guarda **os itens, pistas e marcadores** definidos no `CatalogoDeItens` (37 ids, por exemplo, Cartão do IML, Pé de Cabra, Diário de Gabriel e Gravação de Gabriel).

Todos os itens são cadastrados no início da partida com status *desabilitado*. O jogador não "cria" itens: ele os **obtém** quando uma escolha com efeito `ADICIONAR_ITEM` é selecionada, e o item passa a contar como possuído. Os itens se organizam por tipo:

| Tipo | Exemplos | Uso |
|---|---|---|
| Acesso | Cartão do IML, Chave Mestra do Cartório, Pé de Cabra | Liberam opções que dependem de entrar em um local |
| Evidência | Laudo Adulterado, Diário de Gabriel, Gravação de Gabriel | Provas da investigação; também exigidas nos finais |
| Pista | Canal do Gabinete, Nome do Assessor | Registram o que foi descoberto em interrogatórios |
| Equipamento | Rádio da Polícia, Pistola Tática | Habilitam ações específicas |
| Status | Procurado por Agressão, Alarme Disparado | Marcam consequências de decisões passadas |
| Marcador de rota | Rota Ativada: A Queda pelo Direito (e as outras duas) | Indicam a rota do Ato V |

Um item é **usado** como requisito de acesso de uma escolha (ver 5.4). Os itens não são consumidos: o `Inventario` apenas habilita ou desabilita o item. O botão **"Status e inventário"** mostra atributos, relacionamentos e itens a qualquer momento.

### 5.4. Escolhas, consequências e condições de acesso

Cada `Escolha` pode ter **condições de acesso** (item possuído, atributo mínimo ou confiança mínima com um NPC), que o `EstadoPartida.motivoDeBloqueio` confere antes de liberar a opção, e **efeitos** (`Efeito`), que alteram atributos, confiança de NPCs ou inventário, ou encerram a partida (*game over*). Quando uma opção está bloqueada, o jogador vê o motivo, como "Você não possui confiança suficiente com Helena".

Exemplos reais do jogo:

* **Atributo mínimo (Ato IV, escolha decisiva):** as três opções que levam ao Ato V exigem **15 pontos** no atributo da rota (Razão, Paranoia ou Violência). Quem não atinge nenhum dos limites só tem a opção de tentar um discurso sem provas, que resulta em *game over*.
* **Confiança com NPC (Ato IV):** a opção de pedir que Helena distraia Daniel exige **confiança ≥ 70** com ela. Com confiança menor, a mesma ação leva a um *game over*.
* **Atributo + item (Ato VI):** cada final exige **24 pontos** no atributo da rota *e* um item específico: *A Balança* pede o Diário de Gabriel (item 10), *A Névoa* pede a Gravação de Gabriel (item 11) e *O Preço do Sangue* pede o Laudo Adulterado (item 8).
* **Efeitos:** as opções de caráter dão +3 no atributo correspondente e, na decisão do Ato IV, ainda concedem um item de marcador de rota (ids 35, 36 e 37).

Quando "Mostrar efeitos das escolhas" está ativado nas configurações, as consequências numéricas são exibidas após cada escolha.

▶️ [Inserir aqui o vídeo da restrição de acesso]

### 5.5. Finais

<details>
  <summary> Visualizar finais </summary>

  O jogo possui 3 finais distintos, ligados às rotas narrativas. A rota é escolhida na decisão do Ato IV (Razão, Paranoia ou Violência) e o final é escolhido no Ato VI, a partir do atributo e do item exigidos. A rota define a cena do Ato V.

  | Final | Rota | Condição no Ato VI |
  |---|---|---|
  | A Balança (Razão) | Ato V, "A Queda pelo Direito": o Prefeito é preso em plenário, com as provas expostas à Polícia Federal | Razão ≥ 24 e Diário de Gabriel |
  | A Névoa se Levanta (Paranoia) | Ato V, "A Revolta da Cidade": a população cerca a câmara e derruba o governo | Paranoia ≥ 24 e Gravação de Gabriel |
  | O Preço do Sangue (Violência) | Ato V, "A Sentença de Sangue": o Prefeito é forçado a assinar a confissão sob ameaça de arma | Violência ≥ 24 e Laudo Adulterado |

  Além dos três finais, existem desfechos de *game over*, como "O Silêncio de Santa Aurora" (abandonar a cidade no Ato VI), que não são contados como final.

</details>

### 5.6. Sistema de saves (Fase 2)

* **Múltiplos slots:** 3 slots manuais (1 a 3) e 1 slot de salvamento automático (0), gravados em `saves/` como `slot1.json`, `slot2.json`, `slot3.json` e `autosave.json`;
* **Salvamento automático:** ocorre ao **entrar em cada ponto de salvamento** (todos os atos, exceto Prólogo, Final e Fim) e só o jogo grava nesse slot. A opção **Continuar partida** retoma o save mais recente entre os slots;
* **Salvamento manual:** oferecido no mesmo ponto, em que o jogador escolhe entre continuar, salvar ou voltar ao menu;
* **Sobrescrita e exclusão:** salvar em um slot ocupado pede confirmação antes de sobrescrever; os saves podem ser excluídos (também com confirmação) em **Carregar / gerenciar saves**;
* **Tratamento de falhas:** todas as falhas de persistência derivam de `PersistenciaException`. O `Controlador` captura cada uma, avisa o jogador e segue sem encerrar o programa:

| Exceção | Quando é lançada |
|---|---|
| `SaveException` | Falha ao gravar ou excluir (disco cheio, sem permissão, slot inválido) |
| `CarregamentoException` | Falha ao ler um save (slot vazio ou slot inválido, arquivo ilegível) |
| `DadosInvalidosException` (subclasse de `CarregamentoException`) | Arquivo lido, mas sem conteúdo válido (JSON malformado, campo faltando, valor fora do intervalo, item inexistente, versão incompatível) |
| `CenaInvalidaException` | JSON de cena ausente, malformado ou com personagem, item, atributo ou efeito desconhecido |
| `PartidaInterrompidaException` (`RuntimeException`) | O jogador volta ao menu no meio da história (Fase 3); tratada em `executarHistoria` |
### 5.7. Configurações persistentes (Fase 2)

Três preferências do jogador são gravadas em JSON e restauradas ao abrir o jogo:

| Preferência | Função |
|---|---|
| Personagem favorito | Após *Nova partida*, permite usar o personagem salvo ou criar um novo |
| Mostrar efeitos das escolhas | Exibe (ou oculta) as consequências numéricas de cada escolha |
| Pausa entre diálogos | Controla o ritmo da exibição dos diálogos |

### 5.8. Conquistas e conteúdo desbloqueável (Fase 2)

O registro de finais alimenta a opção **Conquistas** do menu: uma conquista por final (Razão, Paranoia e Violência) e a conquista **Platina**, concedida ao atingir todos os finais.

### 5.9. Interface gráfica (Fase 3)

A interface usa JavaFX com telas em FXML: menu inicial, criação do protagonista, tela da narrativa (arte da cena, personagem, texto, opções, status e botão de voltar ao menu), créditos e instruções. Saves, configurações e conquistas aparecem em janelas de diálogo do JavaFX.

**Comunicação com o MVC:** os controllers das telas só encaminham cliques. O `MenuController` pede ao `Controlador` o que é regra do jogo (nova partida, continuar, saves, conquistas, configurações). Durante a história, o `Controlador` publica eventos no `EventBus` e a `ViewFX` mostra o que recebeu na tela do jogo (`JogoController`), devolvendo o número da opção clicada. Nenhuma regra da narrativa, save ou persistência está no FXML nem nos controllers das telas.

**Uma thread só:** o loop da história no `Controlador` espera a resposta do jogador. Na tela do jogo, `Platform.enterNestedEventLoop` segura a história até o clique; o clique chama `Platform.exitNestedEventLoop` e ela continua de onde parou. Para voltar ao menu no meio da partida, o `JogoController` faz a história lançar `PartidaInterrompidaException`, que o `Controlador` trata em `executarHistoria`.

**Imagens das cenas:** os PNGs ficam em `resources/images/cenas` (proporção 3:1, ideal 1964x654). A tabela `src/CatalogoDeImagens.java` associa cada diálogo (`idDaCena_NN`) a um arquivo; se não constar, o jogo procura `idDaCena_NN.png`, depois `idDaCena.png`, e por fim mostra um painel de texto. Veja `resources/images/cenas/LEIA-ME.md`.

---

## 6. Testes, Limitações e Melhorias Futuras

### 6.1. Testes de unidade

Os testes cobrem [PREENCHER: classes/regras testadas, ex.: efeitos de escolhas, condições de acesso, cálculo de relacionamento, serialização e desserialização do `EstadoPartida`].

Para executar: [COMANDO ou instrução na IDE, ex.: `mvn test`].

### 6.2. Limitações conhecidas

- [PREENCHER: ex.: conteúdo narrativo limitado a N cenas]
- [PREENCHER: ex.: ausência de áudio/imagens na interface]
- [PREENCHER: outras limitações reais]

## 7. Instruções de Reprodução

### Pré-requisitos

* JDK 17 ou superior;
* Apache Maven (baixa JavaFX 21, Gson e JUnit 5 automaticamente).

### Passo a passo

1. Clone o repositório:
   ```
   git clone https://github.com/[USUARIO]/[REPOSITORIO].git
   ```
2. Entre na pasta do projeto (onde está o `pom.xml`);
3. Para rodar os testes de unidade: `mvn clean test`;
4. Para jogar com a interface gráfica: `mvn javafx:run` (classe `PartidaFX`);
5. Para jogar pelo terminal: `mvn exec:java` (classe `Partida`).

> Os arquivos de save e de configuração são gerados em `saves/` na primeira execução.

---

## 8. Guia de Uso

### 8.1 Menu inicial

| Opção | Função |
|---|---|
| Nova partida | Inicia uma nova história (usando o personagem favorito ou criando um novo) |
| Continuar partida | Retoma o salvamento automático |
| Carregar / gerenciar saves | Carrega ou exclui saves dos slots |
| Instruções | Explica como jogar |
| Conquistas | Mostra as conquistas desbloqueadas |
| Configurações | Altera as preferências persistentes |
| Créditos | Exibe a dupla e a disciplina |
| Sair | Encerra o jogo |


### 8.2 Durante a partida

Clique no botão da opção desejada (ou em "Continuar" quando não há escolha). Em cada ato aparece um ponto de salvamento: continuar, salvar manualmente ou voltar ao menu. O botão "Status e inventário" mostra atributos, relacionamentos e itens, e "Voltar ao menu" encerra a partida (com confirmação).

---

## 9. Vídeos de Demonstração da Execução

Os vídeos a seguir demonstram o funcionamento do jogo.

### 10.1 [TÍTULO, ex.: Menu, criação do protagonista e início da história]

[DESCRIÇÃO CURTA do que o vídeo mostra]

[LINK ou arquivo do vídeo]

### 10.2 [TÍTULO, ex.: Escolhas, consequências e relacionamentos]

[DESCRIÇÃO CURTA do que o vídeo mostra]

[LINK ou arquivo do vídeo]

### 10.3 [TÍTULO, ex.: Saves, configurações e conquistas]

[DESCRIÇÃO CURTA do que o vídeo mostra]

[LINK ou arquivo do vídeo]

### 10.4 [TÍTULO, ex.: Os três finais]

[DESCRIÇÃO CURTA do que o vídeo mostra]

[LINK ou arquivo do vídeo]

> No GitHub, para exibir o vídeo direto no README, edite o README pelo navegador e arraste o arquivo de vídeo para a caixa de edição; o GitHub gera o link `https://github.com/user-attachments/assets/...` automaticamente.

---

## 11. Referências

[PREENCHER no padrão da disciplina/SBC. Exemplos de itens possíveis:]

* Documentação oficial do Java (Oracle).
* Documentação do JavaFX (OpenJFX).
* Documentação da biblioteca JSON utilizada.
* GAMMA, E.; HELM, R.; JOHNSON, R.; VLISSIDES, J. *Padrões de Projeto: Soluções Reutilizáveis de Software Orientado a Objetos*. Bookman.
* Material de aula da disciplina EXA863.
