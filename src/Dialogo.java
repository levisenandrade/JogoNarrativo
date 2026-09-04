public class Dialogo {

    private Personagem personagem;
    private String texto;
    private Escolha[] escolhas;

    // Construtor de diálogo sem opções
    public Dialogo(Personagem personagem, String texto) {
        this.personagem = personagem;
        this.texto = texto;

        this.escolhas = null;
    }

    // Construtor de diálogo com opções
    public Dialogo(
            Personagem personagem,
            String texto,
            Escolha escolha1,
            Escolha escolha2,
            Escolha escolha3
    ) {
        this.personagem = personagem;
        this.texto = texto;

        this.escolhas = new Escolha[3];

        this.escolhas[0] = escolha1;
        this.escolhas[1] = escolha2;
        this.escolhas[2] = escolha3;
    }

    public void executarDialogo() {

        // O nome do narrador não deve ser exibido
        if (!personagem.getNome().equalsIgnoreCase("Narrador")) {
            System.out.println(personagem.getNome() + ":");
        }

        System.out.println(texto);

        if (possuiOpcoes()) {
            for (Escolha escolha : escolhas) {
                System.out.println(escolha.getTexto());
            }
        }
    }

    public boolean possuiOpcoes() {
        return escolhas != null;
    }

    public Escolha getEscolha(int indice) {
        return escolhas[indice - 1];
    }
}
