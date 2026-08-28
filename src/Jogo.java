public class Jogo {

    private Menu menu = new Menu();

    public void novoJogo() {
        int opcao_escolhida = -1;

        while (opcao_escolhida != 3) {
            printaOpcoesIniciais();
            opcao_escolhida = menu.validaOpcao();

            switch (opcao_escolhida) {
                case 1:
                    System.out.println("\nPartida iniciada\n");
                    iniciarPartida();
                    break;

                case 2:
                    mostraInstrucoes();
                    break;

                case 3:
                    System.out.println("Saída");
                    break;

                default:
                    System.out.println("\nOpção inválida\n");
                    break;
            }
        }
    }

    public void printaOpcoesIniciais() {
        System.out.println("\n--- Menu ---\n");
        System.out.println("1 - Iniciar partida");
        System.out.println("2 - Instruções");
        System.out.println("3 - Sair\n");
    }

    public void mostraInstrucoes() {
        System.out.println("""
                \n===== INSTRUÇÕES =====
                
                Bem-vindo ao nosso jogo narrativo interativo!
                
                Você assumirá o papel do protagonista e deverá tomar decisões
                ao longo da história. Suas escolhas poderão alterar os
                acontecimentos, os relacionamentos com outros personagens,
                o acesso a determinadas cenas e até mesmo o final da história.
                
                Durante a aventura, fique atento às informações apresentadas
                e pense bem antes de escolher. Algumas consequências podem
                não ser percebidas imediatamente.
                
                O jogo é baseado em narrativa e escolhas. Não existe apenas
                um caminho correto: diferentes decisões podem levar a
                diferentes acontecimentos e finais.
                
                Boa sorte e boa história!
                """);
    }

    public void iniciarPartida() {
        Personagem mc = new Personagem("Enoch", 320, "Masculino", "Humano", 0);
        // mc = mc.criaPersonagem();

        String textoPrologo = """
                PRÓLOGO
                Você acorda e se vê num lugar desolado... Ao longe, pode ver uma cidade flutuante...
                """;

        System.out.println(textoPrologo);

        Cena cena1 = new Cena(
                "Você vê uma figura de costas andando vagarosamente.",
                "Seguir a figura",
                "Chamar a figura",
                "Ignorar a figura",
                menu
        );

        int opcao_c1 = cena1.executarCena();

        switch (opcao_c1) {
            case 1:
                Cena cena11 = new Cena(
                        "A figura lhe escuta ao passar. Você fica inseguro de dar as costas pra ela.",
                        "Sacar espada",
                        "Permanecer parado",
                        "Fugir",
                        menu
                );

                cena11.executarCena();
                break;

            case 2:
                Cena cena12 = new Cena(
                        "A figura lhe encara, apesar de não ter olhos. Ela pronuncia:\n ..."
                                + mc.getNome() + "...",
                        "Sacar espada",
                        "Permanecer parado",
                        "Fugir",
                        menu
                );

                cena12.executarCena();
                break;

            case 3:
                Cena cena13 = new Cena(
                        "A figura emite um som irreconhecível. Você sente um arrepio na espinha.",
                        "Sacar espada",
                        "Permanecer parado",
                        "Fugir",
                        menu
                );

                cena13.executarCena();
                break;

            default:
                System.out.println("Opção inválida");
                break;
        }
    }
}
