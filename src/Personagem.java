public class Personagem {
    private String nome;
    private int idade;
    private String genero;

    public Personagem(String nome, int idade, String genero) {
        this.nome = nome;
        this.idade = idade;
        this.genero = genero;
    }

    public String getNome() {
        return this.nome;
    }
}
