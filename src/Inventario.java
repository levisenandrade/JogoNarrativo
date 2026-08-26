public class Inventario {
    int max_cap;
    Item[] inventario;

    public Inventario(int max_cap) {
        this.max_cap = max_cap;
        this.inventario = new Item[max_cap];
    }
    public void adicionarItem(Item item, int quantidade) {
        //Já vão existir todos os intens do jogo em um array e aq a gente só adiciona?
        int i = 0;
        while (i < max_cap) {
            if (inventario[i] == null) {
                inventario[i] = item;
                System.out.println("Você achou " + quantidade + " " + item.getNome());
                return;
            }

            i++;
        }
    }
    //Esses dois métodos vão existir mas eu n sei se ainda qual o melhor jeito de apagar, se seria percorrendo a lista de itens do inventário e comparando ou se a gente usa o id que tem lá em item e apaga direto
    public void consumirItem(Item item, int quantidade){
        System.out.println("Você Consumiu " + quantidade +" "+item.getNome());
    //Aq em consumir provavelmente a gente vá usar algo da classe atributos, pra modificar o andamento da história 
    }

    public void  descartarItem(Item item, int quantidade){
        System.out.println("Você descartou " + quantidade +" "+item.getNome() +" do seu inventário");
    }

}
