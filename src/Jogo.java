import java.util.Scanner;

public class Jogo {
    public static void main(String[] args) {
        Jogo jogo = new Jogo();

        int opcao_escolhida = -1;
        while (opcao_escolhida != 3) {
            jogo.printaOpcoesIniciais();

            opcao_escolhida = jogo.recebeOpcao();

            switch (opcao_escolhida) {
                case 1:
                    // caso 1
                    System.out.println("\nPartida iniciada\n");
                    jogo.iniciarPartida();
                    break;
                case 2:
                    // caso 2
                    jogo.mostraInstrucoes();
                    break;
                case 3:
                    // caso 3
                    System.out.println("Saída");
                    break;

                default:
                    // demais casos
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

    public int recebeOpcao() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite a opcao desejada: ");
        int opcao = scanner.nextInt();

        return opcao;
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
        Personagem enoch = new Personagem("Enoch", 320, "Masculino", "Humano", 0);

        String texto = """
                Você se depara com uma figura andando pelos corredores.
                
                1 - Seguir a figura
                2 - Chamar a figura
                3 - Ignorar a figura e se esgueirar
                """;

        String mensagem1 = "A figura emite um som de estalo com a língua. Você sente um arrepio na espinha.\n";
        String mensagem2 = "A figura lhe encara, apesar de não ter olhos. Ela pronuncia:\n ..." + enoch.getNome() + "...";
        String mensagem3 = "A figura lhe escuta ao passar. Você fica inseguro de dar as costas pra ela.";

        menuEscolha();
    }

    public int menuEscolha() {
        // o propósito desse "menu atualizado" que substituirá o "menuEscolha" anterior é para melhor modularizar o código
        // nesse caso, o menu de escolha não vai mais receber nem printar mensagens de escolhas, ele somente se encarrega de retonar o valor da escolha se for válida

        int opcao = recebeOpcao();
        boolean valida = false;

        while (!valida) {
            if (opcao == 1 || opcao == 2 || opcao == 3) {
                valida = true;
                return opcao;
            } else {
                System.out.println("Escolha inválida.");
                opcao = recebeOpcao();
            }
        }
        return opcao;
    } // retorna a opção escolhida que vai ser utilizada em outro módulo de controle da narrativa

}