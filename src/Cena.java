public class Cena {
    // Classe Cena recebe apenas o texto da cena e as opções disponíveis
    // visto que eventualmente uma consequência pode ser mais que só mostrar a mensagem

    private String texto; // diálogo principal
    private String opcao1; // opção 1
    private String opcao2; // opção 2
    private String opcao3; // opção 3
    private Menu menu;

    public Cena(String texto, String op1, String op2, String op3, Menu menu) {
        // depois ver como faço pra colocar valores default na escolha, ou seja, strings em branco
        this.texto = texto;
        this.opcao1 = op1;
        this.opcao2 = op2;
        this.opcao3 = op3;
        this.menu = menu;
    }

    public int executarCena() {

        System.out.println("\n" + this.texto + "\n");
        System.out.println("1 - " + this.opcao1 + "\n");
        System.out.println("2 - " + this.opcao2 + "\n");
        System.out.println("3 - " + this.opcao3 + "\n");

        return menu.validaOpcao();

    }
}
