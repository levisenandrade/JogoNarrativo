public class Cena {

    private Dialogo[] arrayDialogos;
    private int dialogoAtual;

    // Construtor com tamanho padrão de diálogos
    public Cena() {
        this.arrayDialogos = new Dialogo[5];
        this.dialogoAtual = 0;
    }

    // Construtor para quantidades maiores de diálogos
    public Cena(int quantidadeDialogos) {
        this.arrayDialogos = new Dialogo[quantidadeDialogos];
        this.dialogoAtual = 0;
    }

    // Criação de diálogo que possui somente texto
    public void adicionaDialogoSemOpcoes(
            Personagem personagem,
            String texto
    ) {

        if (dialogoAtual < arrayDialogos.length) {

            arrayDialogos[dialogoAtual] =
                    new Dialogo(
                            personagem,
                            texto
                    );

            dialogoAtual += 1;
        }
    }

    // Criação de diálogo que possui texto, opções e consequências
    public void adicionaDialogoComOpcoes(
            Personagem personagem,
            String texto,
            Escolha escolha1,
            Escolha escolha2,
            Escolha escolha3
    ) {

        if (dialogoAtual < arrayDialogos.length) {

            arrayDialogos[dialogoAtual] =
                    new Dialogo(
                            personagem,
                            texto,
                            escolha1,
                            escolha2,
                            escolha3
                    );

            dialogoAtual += 1;
        }
    }

    public void executaDialogos() {

        for (int i = 0; i < dialogoAtual; i++) {

            Dialogo dialogo = arrayDialogos[i];

            dialogo.executarDialogo();
        }
    }

    public Dialogo getDialogo(int indice) {
        return arrayDialogos[indice];
    }

    public int getQuantidadeDialogos() {
        return dialogoAtual;
    }

}
