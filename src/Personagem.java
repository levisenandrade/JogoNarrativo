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
