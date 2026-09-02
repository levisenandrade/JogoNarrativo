public class Dialogo {

    private Entrada entrada;
    private Personagem personagem;
    private String texto;
    private String opcao1;
    private String opcao2;
    private String opcao3;
    private int opcaoEscolhida;

    private String[] arrayDeAtributosAfetados;
    private int[] arrayDeValoresCorrespondentes;

    // Construtor de diálogo sem opções
    public Dialogo(Personagem personagem, String texto, Entrada entrada) {
        this.personagem = personagem;
        this.entrada = entrada;
        this.texto = texto;

        this.opcao1 = null;
        this.opcao2 = null;
        this.opcao3 = null;
    }

    // Construtor de diálogo com opções
    public Dialogo(
            Personagem personagem,
            String texto,
            String opcao1,
            String opcao2,
            String opcao3,
            String[] atributos,
            int[] valores,
            Entrada entrada
    ) {
        this.personagem = personagem;
        this.entrada = entrada;
        this.texto = texto;

        this.arrayDeAtributosAfetados = atributos;
        this.arrayDeValoresCorrespondentes = valores;

        this.opcao1 = opcao1;
        this.opcao2 = opcao2;
        this.opcao3 = opcao3;
    }

    public void executarDialogo() {
        // verifica se o nome do personagem que emite a frase é diferente de "Narrador"
        // o nome do narrador não deve ser exibido

        if (!personagem.getNome().equalsIgnoreCase("Narrador")) {
            System.out.println(personagem.getNome() + ":");
        }

        System.out.println(texto);

        if (opcao1 != null) {
            System.out.println(opcao1);
            System.out.println(opcao2);
            System.out.println(opcao3);

            this.opcaoEscolhida = validaOpcao();
        }
    }

    public int validaOpcao() {
        int opcao = recebeOpcao();
        boolean invalida = true;

        while (invalida) {
            if (opcao == 1 || opcao == 2 || opcao == 3) {
                invalida = false;
            } else {
                System.out.println("Escolha inválida.");
                opcao = recebeOpcao();
            }
        }

        return opcao;
    }

    public int recebeOpcao() {
        System.out.println("Digite a opção desejada: ");
        return entrada.lerInteiro();
    }

    public int getEscolha() {
        return opcaoEscolhida;
    }

    public boolean possuiOpcoes() {
        return opcao1 != null;
    }

    public String getAtributo() {
        return arrayDeAtributosAfetados[opcaoEscolhida - 1];
    }

    public int getValor() {
        return arrayDeValoresCorrespondentes[opcaoEscolhida - 1];
    }

}
