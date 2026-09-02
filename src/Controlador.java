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

        Cena prologo = new Cena(entrada);

        Personagem narrador =
                new Personagem(
                        "Narrador",
                        0,
                        "Neutro"
                );

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

        prologo.executaDialogos();
    }
}
