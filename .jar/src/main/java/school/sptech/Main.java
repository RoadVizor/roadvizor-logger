package school.sptech;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        Logs teste = new Logs();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insira true or false");
        Boolean lets = scanner.nextBoolean();
        teste.listaAleatorios[0] = 81.0;
        teste.listaAleatorios[1] = 78.0;
        teste.listaAleatorios[2] = 55.0;

        Thread jar = new Thread(() -> {

         teste.Horario();

       teste.criacaoServer(lets);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Erro");;
            }


            teste.capturaDados(lets);
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e){
                System.out.println("Erro");
            }

            teste.tratamento(lets);



        });
        jar.start();
    }
}
