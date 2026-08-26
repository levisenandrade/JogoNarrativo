public class Item {
    private String item_nome;
    private String item_tipo;
    private String item_descricao;
    private int item_id;
    private int item_quant;

    public Item(String item_nome, String item_tipo, String item_descricao, int item_id, int item_quant) {
        this.item_nome = item_nome;
        this.item_tipo = item_tipo;
        this.item_descricao = item_descricao;
        this.item_id = item_id;
        this.item_quant = item_quant;
    }

    public String getNome() {
        return this.item_nome;
    }

    public int getQuant() {
        return this.item_quant;
    }

    public String getDescricao() {
        return this.item_descricao;
    }

    public void Consumir(int quantidade) {
        if (item_quant >= quantidade) {
            this.item_quant -= quantidade;
            System.out.println("Você Consumiu: " + getNome());
        } else {
            System.out.println("Você não tem " + getNome() + " suficientes para isso!");
        }
    }

    public void Incrementar(int quantidade) {
        //if(item<0) tem que criar o item aq? ou só pode incrementar se já existir o item no inventário
        this.item_quant += quantidade;
        System.out.println("Você coleutou " + quantidade + getNome());
    }
}
