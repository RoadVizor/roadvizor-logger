package shool.sptech;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

public class LeituraBucket {

    private S3Client s3Client;

    public LeituraBucket() {

        // Cria o coiso para acessar o S3
        s3Client = S3Client.builder()

                // Define a região da AWS
                .region(Region.US_EAST_1)

                // Busca automaticamente as credenciais configuradas
                .credentialsProvider(DefaultCredentialsProvider.create())

                // Finaliza a criação do cliente
                .build();
    }

    public void listarArquivos(String nomeBucket) {

        // Cria uma requisição para listar os arquivos
        // que estão dentro do bucket
        ListObjectsRequest requisicao = ListObjectsRequest.builder()
                .bucket(nomeBucket)
                .build();

        // Envia a requisição para a AWS
        // e pega os arquivos encontrados
        List<S3Object> arquivos =
                s3Client.listObjects(requisicao).contents();

        System.out.println("================================");
        System.out.println("Arquivos do bucket:");
        System.out.println("================================");

        // Percorre todos os arquivos encontrados
        for (S3Object arquivo : arquivos) {

            // Mostra o nome do arquivo
            System.out.println(arquivo.key());
        }

        System.out.println("================================");
    }
}