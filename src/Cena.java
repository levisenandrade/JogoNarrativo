public class Cena {

    private Dialogo[] arrayDialogos;
    private int dialogoAtual;
    private Entrada entrada;

    // Construtor com tamanho padrão de diálogos
    public Cena(Entrada entrada) {
        this.arrayDialogos = new Dialogo[5];
        this.dialogoAtual = 0;
        this.entrada = entrada;
    }

    // Construtor para quantidades maiores de diálogos
    public Cena(int quantidadeDialogos, Entrada entrada) {
        this.arrayDialogos = new Dialogo[quantidadeDialogos];
        this.dialogoAtual = 0;
        this.entrada = entrada;
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
                            texto,
                            entrada
                    );

            dialogoAtual += 1;

        } else {
            System.out.println(
                    "Limite de dialogos atingido para esse turno!!!"
            );
        }
    }

    // Criação de diálogo que possui texto, opções e consequências
    public void adicionaDialogoComOpcoes(
            Personagem mc,
            String texto,
            String op1,
            String op2,
            String op3,
            String[] atributos,
            int[] valores
    ) {

        if (dialogoAtual < arrayDialogos.length) {

            arrayDialogos[dialogoAtual] =
                    new Dialogo(
                            mc,
                            texto,
                            op1,
                            op2,
                            op3,
                            atributos,
                            valores,
                            entrada
                    );

            dialogoAtual += 1;

        } else {
            System.out.println(
                    "Limite de dialogos atingido para esse turno!!!"
            );
        }
    }

    public void executaDialogos() {

        for (int i = 0; i < dialogoAtual; i++) {

            Dialogo d = arrayDialogos[i];

            d.executarDialogo();
        }
    }

    public void aplicaEfeitos(
            Protagonista protagonista,
            String nomeAtributo,
            int valor
    ) {

        if (protagonista != null) {
            protagonista.alteraAtributo(nomeAtributo, valor);
        } else {
            System.out.println("Erro: Personagem inválido.");
        }
    }
}
