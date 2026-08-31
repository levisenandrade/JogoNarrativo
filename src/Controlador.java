import java.util.Scanner;

public class Controlador {

    public void opcoesIniciais() {
        int opcao_escolhida = -1;

        while (opcao_escolhida != 3) {
            System.out.println("\n--- Menu ---\n");
            System.out.println("1 - Iniciar partida");
            System.out.println("2 - Instruções");
            System.out.println("3 - Sair\n");

            opcao_escolhida = recebeOpcao();

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

    public int recebeOpcao() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite a opcao desejada: ");
        return scanner.nextInt();
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
        Cena prologo = new Cena();
        Personagem narrador= new Personagem("Narrador",0,"Neutro");
        prologo.adicionarDialgoSemOpcoes("""
                Silêncio.
                
                Por alguns segundos, nenhum som.
                Então, lentamente, começa a chover.Primeiro algumas gotas.Depois, uma chuva constante. Ao fundo, quase imperceptível, o som de carros passando sobre o asfalto molhado.Um cachorro late. Uma porta se fecha. Uma televisão pode ser ouvida de dentro de alguma casa. Pouco a pouco, os sons de uma cidade começam a se formar.Um rádio é ligado:
                
                =============
                Rádio:
                	"...e a polícia confirmou nesta manhã a identidade da quinta vítima encontrada morta nos últimos seis meses."
                
                =============
                Rádio:
                	"O caso volta a chamar atenção dos moradores de Santa Aurora, que começam a questionar a segurança na cidade.\"""");
        prologo.executaDialogos(narrador);
        Personagem mc = new Personagem();
        prologo.adicionarDialgoSemOpcoes("""
                Uma cidade é vista de longe.O céu está encoberto.A chuva cai sobre os prédios.Uma fina camada de névoa cobre parte das ruas.No centro, prédios antigos dividem espaço com construções mais recentes.Carros passam pelas avenidas.Um ônibus para em um ponto.Pessoas caminham apressadas, protegendo-se da chuva com guarda-chuvas e casacos.Algumas lojas começam a abrir.
                Uma padaria recebe os primeiros clientes da manhã.Na frente de uma banca de jornal, algumas pessoas observam as manchetes.Uma viatura da polícia passa lentamente por uma avenida.
                O som da chuva continua...
                
                ============
                Rádio*
                	"A vítima, cuja identidade foi preservada pelas autoridades até que os familiares fossem comunicados, foi encontrada durante a madrugada em uma residência abandonada na região norte."
                Uma rua comercial.
                
                """);
        prologo.executaDialogos(mc);
    }
    public int menuEscolha() {
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
    }
}
