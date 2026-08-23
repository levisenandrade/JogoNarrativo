import java.util.Scanner;

public class Jogo {
    public static void main(String[] args) {
        Jogo jogo = new Jogo();

        int opcao_escolhida = -1;
        while (opcao_escolhida != 3) {
            jogo.printaOpcoes();

            opcao_escolhida = jogo.recebeOpcao();

            switch (opcao_escolhida) {
                case 1:
                    // caso 1
                    System.out.println("\nPartida iniciada\n");
                    break;
                case 2:
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
                    // caso 2
                    break;
                case 3:
                    System.out.println("3");
                    // caso 3
                    break;
            }
        }
    }

    public void printaOpcoes() {
        System.out.println("--- Menu ---\n");
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

}