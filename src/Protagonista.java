public class Protagonista extends Personagem {
    private Atributo[] atributos;

    public Protagonista(String nome, int idade, String genero) {
        super(nome, idade, genero);

        atributos = new Atributo[3];

        atributos[0] = new Atributo("Razão", 50, 100);
        atributos[1] = new Atributo("Paranoia", 50, 100);
        atributos[2] = new Atributo("Violencia", 50, 100);
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

    public int getAtributo(String nomeAtributo) {
        for (int i = 0; i < atributos.length; i++) {
            if (atributos[i].getNome().equalsIgnoreCase(nomeAtributo)) {
                return atributos[i].getValor();
            }
        }
        return 0;
    }
}
