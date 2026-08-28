import java.util.Scanner;

public class Menu {

    public int recebeOpcao() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite a opcao desejada: ");
        return scanner.nextInt();
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
}
