import java.util.Scanner;
public class Personagem {
    private String nome;
    private int idade;
    private String sexo;
    private String raca;
    private int afinidade;

    public Personagem(String nome,  int idade, String sexo, String raca, int afinidade) {

        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.raca = raca;
        this.afinidade = afinidade;

    }
    public Personagem criaPersonagem() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o nome do seu Personagem:");
        String novo_nome = scanner.nextLine();

        System.out.println("Digite sua idade:");
        int nova_idade = scanner.nextInt();

        System.out.println("Digite o sexo do personagem:");
        String novo_sexo = scanner.nextLine();

        System.out.println("Digite a raça do personagem:");
        String novo_raca = scanner.nextLine();

        System.out.println("Digite a afinidade do personagem (número):");
        int novo_afinidade = scanner.nextInt();
        scanner.nextLine(); // Consome a quebra de linha pendente

        return new Personagem(novo_nome, nova_idade, novo_sexo, novo_raca, novo_afinidade);
    }
    public String getNome() {
        return this.nome;
    }

    public int getAfinidade() {
        return this.afinidade;
    }

    public void aumentaAfinidade(int quantidade) {
        this.afinidade += quantidade;
    }

    public void reduzAfinidade(int quantidade) {
        this.afinidade -= quantidade;
    }
}
