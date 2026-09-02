public class Atributo {
    private String nome;
    private int valor;

    public Atributo(String nome, int valor, int valorMaximo) {
        this.nome = nome;

        if (valor < 0) {
            this.valor = 0;
        } else if (valor > valorMaximo) {
            this.valor = valorMaximo;
        } else {
            this.valor = valor;
        }
    }

    public String getNome() {
        return this.nome;
    }

    public int getValor() {
        return this.valor;
    }

    public void aumentaValor(int quantidade) {
        if (quantidade > 0) {
            this.valor += quantidade;
        }
    }

    public void reduzValor(int quantidade) {
        if (quantidade > 0) {
            this.valor -= quantidade;

            if (this.valor < 0) {
                this.valor = 0;
            }
        }
    }
}
