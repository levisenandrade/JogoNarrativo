public class Cena {
    private Dialogo[] arrayDialogos;
    private int dialogoAtual;

    //Construtor com Tamanho padrão de dialogos
    public Cena() {
        this.arrayDialogos = new Dialogo[5];
        this.dialogoAtual = 0;
    }

    //Construtor para quantiades maiores de dialogos
    public Cena(int quantidadeDialogos) {
        this.arrayDialogos = new Dialogo[quantidadeDialogos];
        this.dialogoAtual = 0;
    }


    //Criação de Dialogo que possui somente Texto
    public void adicionaDialogoSemOpcoes(String texto) {
        if (dialogoAtual < arrayDialogos.length) {
            arrayDialogos[dialogoAtual] = new Dialogo(texto);
            dialogoAtual += 1;
        } else {
            System.out.println("Limite de dialogos atingido para esse turno!!!");
        }
    }

    //Criação de Dialogo que possui texto, opçções e consequências diretas
        //As consequências de efeitos já são aplicadas dentro do metodo
    public void adicionaDialogoComOpcoes(Personagem mc, String texto, String op1, String op2, String op3, String[] atributos, int[] valores) {
        if (dialogoAtual < arrayDialogos.length) {
            arrayDialogos[dialogoAtual] = new Dialogo(texto, op1, op2, op3,atributos,valores);

            String atributoResultante = arrayDialogos[dialogoAtual].getAtributo();
            int valorResultante = arrayDialogos[dialogoAtual].getValor();

            dialogoAtual += 1;
        } else {
            System.out.println("Limite de dialogos atingido para esse turno!!!");
        }
    }


    public void executaDialogos(Personagem mc) {
        for (int i = 0; i < dialogoAtual; i++) {
            Dialogo d = arrayDialogos[i];
            d.executarDialogo();

            // Só aplica efeitos se o diálogo possuir opções
            if (d.possuiOpcoes()) {

                int valorEfeito = d.getValor();
                String atributoNome = d.getAtributo();

                aplicaEfeitos(mc, atributoNome, valorEfeito);
            }
        }
    }
    public void aplicaEfeitos(Personagem personagem, String nomeAtributo, int valor) {
        if (personagem != null) {
            personagem.alteraAtributo(nomeAtributo, valor);
        } else {
            System.out.println("Erro: Personagem inválido.");
        }
    }

}
