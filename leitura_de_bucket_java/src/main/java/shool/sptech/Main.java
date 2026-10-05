package shool.sptech;

public class Main {
    public static void main(String[] args) {

        LeituraBucket leituraBucket = new LeituraBucket();

        //Aqui ó, tem que ser o nome do bucket
        leituraBucket.listarArquivos(
                "amazon-pasta-bucket-so-899480674341-us-east-1-an"
        );
    }
}