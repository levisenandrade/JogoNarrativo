public class Dialogo {

    private Personagem personagem;
    private String texto;
    private String opcao1;
    private String opcao2;
    private String opcao3;

    private String[] arrayDeAtributosAfetados;
    private int[] arrayDeValoresCorrespondentes;

    // Construtor de diálogo sem opções
    public Dialogo(Personagem personagem, String texto) {
        this.personagem = personagem;
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
            int[] valores
    ) {
        this.personagem = personagem;
        this.texto = texto;

        this.opcao1 = opcao1;
        this.opcao2 = opcao2;
        this.opcao3 = opcao3;

        this.arrayDeAtributosAfetados = atributos;
        this.arrayDeValoresCorrespondentes = valores;
    }

    public void executarDialogo() {

        // O nome do narrador não deve ser exibido
        if (!personagem.getNome().equalsIgnoreCase("Narrador")) {
            System.out.println(personagem.getNome() + ":");
        }

        System.out.println(texto);

        if (possuiOpcoes()) {
            System.out.println(opcao1);
            System.out.println(opcao2);
            System.out.println(opcao3);
        }
    }

    public boolean possuiOpcoes() {
        return opcao1 != null;
    }

    public String getOpcao1() {
        return opcao1;
    }

    public String getOpcao2() {
        return opcao2;
    }

    public String getOpcao3() {
        return opcao3;
    }

    public String[] getArrayDeAtributosAfetados() {
        return arrayDeAtributosAfetados;
    }

    public int[] getArrayDeValoresCorrespondentes() {
        return arrayDeValoresCorrespondentes;
    }
}
