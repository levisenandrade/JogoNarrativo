import java.util.Scanner;

public class Personagem {
    private String nome;
    private int idade;
    private String genero;
    private Atributo[] atributos;


    //Construtor padrão para criar o MC
    public Personagem () {
        atributos = new Atributo[3];
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o nome do seu nome:\n");
        this.nome = scanner.nextLine();

        System.out.println("Digite sua idade:\n");
        this.idade = scanner.nextInt();//tem que verificar idade

        System.out.println("Digite o gênero do personagem:");
        this.genero = scanner.nextLine();

        //Criação + Valores padrão para cada atributo do MC
        atributos[0] = new Atributo("Razão", 50,100);
        atributos[1] = new Atributo("Paranoia", 50,100);
        atributos[2] = new Atributo("Violencia", 50,100);
    }

    //Construtor Secundário para criar os demais personagens
    public Personagem(String nome, int idade, String genero) {
        this.nome = nome;
        this.idade = idade;
        this.genero = genero;
    }

    public String getNome() {
        return this.nome;
    }


    public void alteraAtributo(String nomeAtributo, int quantidade) {
        for (int i = 0; i < atributos.length; i++) {
            if (atributos[i].getNome().equalsIgnoreCase(nomeAtributo)) {
                if (quantidade >= 0) {
                    atributos[i].aumentaValor(quantidade);
                } else {
                    atributos[i].reduzValor(Math.abs(quantidade));
                }
                return;
            }
        }
        System.out.println("Atributo não existente");
    }

}
