import java.util.Scanner;

public class Entrada {
    private Scanner scanner;

    public Entrada() {
        scanner = new Scanner(System.in);
    }

    public int lerInteiro() {
        int numero = scanner.nextInt();
        scanner.nextLine(); // remove o Enter do buffer
        return numero;
    }

    public String lerString(){
        return scanner.nextLine();
    }
}
