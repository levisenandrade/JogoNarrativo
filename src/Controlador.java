public class Controlador {

    private Menu menu;
    private Entrada entrada;

    public Controlador() {
        entrada = new Entrada();
        menu = new Menu(entrada);
    }

    public void menuInicial() {

        int opcao;

        do {
            menu.mostraMenu();

            opcao = menu.validaOpcao();

            switch (opcao) {

                case 1:
                    iniciarPartida();
                    break;

                case 2:
                    menu.mostraInstrucoes();
                    break;

                case 3:
                    menu.mostraSaida();
                    break;
            }

        } while (opcao != 3);
    }

    public void iniciarPartida() {

        String nome = menu.recebeNome();
        int idade = menu.recebeIdade();
        String genero = menu.recebeGenero();

        Protagonista protagonista =
                new Protagonista(nome, idade, genero);

        Cena prologo = new Cena();

        Personagem narrador =
                new Personagem(
                        "Narrador",
                        0,
                        "Neutro"
                );

        Escolha escolha1 = new Escolha("1 - Ir até o carro", "Paranoia", 3);
        Escolha escolha2 = new Escolha("2 - Continuar andando", "Razão", 3);
        Escolha escolha3 = new Escolha("3 - Voltar pelo caminho", "Paranoia", -5);

        prologo.adicionaDialogoSemOpcoes(
                narrador,
                """
                Silêncio.
                
                Por alguns segundos, nenhum som.
                Então, lentamente, começa a chover. Primeiro algumas gotas.
                Depois, uma chuva constante.
                
                Ao fundo, quase imperceptível, o som de carros passando
                sobre o asfalto molhado.
                """
        );

        prologo.adicionaDialogoSemOpcoes(
                narrador,
                """
                Uma cidade é vista de longe.
                O céu está encoberto.
                A chuva cai sobre os prédios.
                
                Uma fina camada de névoa cobre parte das ruas.
                """
        );

        prologo.adicionaDialogoSemOpcoes(
                protagonista,
                """
                Onde eu estou?
                """
        );

        prologo.adicionaDialogoComOpcoes(
                narrador,
                """
                Você olha ao redor. A rua está completamente vazia.
                Há um carro estacionado alguns metros à frente.
                """,
                escolha1,
                escolha2,
                escolha3
        );
        // teste de funcionamento para ver se a alteração de valor do atributo realmetne ta funcioandno
        System.out.println(
                "Paranoia: " + protagonista.getAtributo("Paranoia")
        );

        executaCena(prologo, protagonista);

        // teste de funcionamento para ver se a alteração de valor do atributo realmetne ta funcioandno
        System.out.println(
                "Paranoia: " + protagonista.getAtributo("Paranoia")
        );
    }

    public void executaCena(Cena cena, Protagonista protagonista) {

        for (int i = 0; i < cena.getQuantidadeDialogos(); i++) {

            Dialogo dialogo = cena.getDialogo(i);

            dialogo.executarDialogo();

            if (dialogo.possuiOpcoes()) {
                int escolha = menu.recebeEscolha();

                Escolha escolhaSelecionada = dialogo.getEscolha(escolha);

                System.out.println(escolhaSelecionada.getTexto()); // linha para testar se a escolha está realmeten funcionando

                protagonista.alteraAtributo(
                        escolhaSelecionada.getAtributoAfetado(),
                        escolhaSelecionada.getValorAlteracao()
                );
            }
        }
    }
}
