package school.sptech;

import com.fasterxml.jackson.core.JsonProcessingException;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.GetObjectRequest; // monta a requisição para buscar um arquivo específico.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import school.sptech.config.Jira;
import school.sptech.dao.EmpresaDao;
import school.sptech.dto.EmpresaJiraDto;

import java.util.ArrayList;
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

    public List<String> listarArquivos(String nomeBucket, String prefixo) {
        List<String> arquivosNovos = new ArrayList<>();

        // Cria uma requisição para listar os arquivos
        // que estão dentro do bucket
        ListObjectsRequest requisicao = ListObjectsRequest.builder()
                .bucket(nomeBucket)
                .prefix(prefixo)
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
            arquivosNovos.add(arquivo.key());
            // Mostra o nome do arquivo
            System.out.println(arquivo.key());
        }

        System.out.println("================================");
        return arquivosNovos;
    }

    //isa ->

    public String lerArquivo(String nomeBucket, String key, EmpresaDao empresaDao) throws Exception {
        GetObjectRequest requisicao = GetObjectRequest.builder()
                .bucket(nomeBucket)
                .key(key)
                .build();
        // agora estou informando também a key para escolher exatamente qual arquivo desejo ler.

        // forma de tranforma json
        String conteudo =
                s3Client.getObjectAsBytes(requisicao).asUtf8String();

        ObjectMapper mapper = new ObjectMapper();
        //fiz try catch pq o readTree pode lançar uma exeção e o arquivo pode vim quebrado .

        try {
            //ObjectMapper é o objeto que interpreta o JSON.
            //readTree(conteudo) transforma sua String em uma árvore de dados.
            //raiz representa o JSON inteiro, a partir do qual você consegue acessar os campos.
            JsonNode raiz = mapper.readTree(conteudo);

            // pega o MAC que está dentro do arquivo
            String mac = raiz.get("endereco_mac").asText();
            System.out.println("MAC: " + mac);

            EmpresaJiraDto config;
            try {
                config = empresaDao.buscarConfigPorMac(mac);
                //pergunta pro banco qual é o Jira da empresa dona desse mac
            } catch (EmptyResultDataAccessException e) {
                System.out.println("MAC não cadastrado no banco: " + mac);
                // se o mac não estiver cadastrado, avisa e segue pro próximo arquivo
                return conteudo;
            }

            Jira jira = new Jira(config.getEmailJira(), config.getDomainJira(), config.getApiTokenJira());

            JsonNode metricas = raiz.get("metricas_monitorizadas");
            // passa por cada métrica, uma de cada vez.
            for (JsonNode metrica : metricas) {
                String componente = metrica.get("componente").asText();
                double valor = metrica.get("valor_medido").asDouble();
                String unidade = metrica.get("unidade_medida").asText();
                boolean alerta = metrica.get("em_alerta").asBoolean();

                System.out.println(
                        "Componente: " + componente +
                                " | Valor medido: " + valor +
                                " " + unidade +
                                " | Alerta: " + alerta);

                // validação do alerta
                if (alerta == true) {

                    // titulo informativo
                    String titulo = "ALERTA CRÍTICO - %s - MAC %s".formatted(componente.toUpperCase(), mac);

                    // descrição mais detalhada sobre a situação do equipamento
                    String descricao = """
                    {noformat}
                    ==============================
                    ALERTA CRÍTICA
                    COMPONENTE: %s
                    VALOR MEDIDO: %.2f %s
                    MAC: %s
                    ==============================
                    {noformat}
                    """.formatted(componente.toUpperCase(), valor, unidade, mac);

                    String resposta = jira.createIssue(config.getProjectKey(), titulo, descricao, "Tarefa");
                    System.out.println(resposta);
                }
            }

        } catch (JsonProcessingException e) {
            // se o arquivo vier quebrado, avisa e segue em frente sem derrubar o programa
            System.out.println("Erro ao interpretar o JSON.");
            System.out.println(e.getMessage());
        }

        return conteudo;
    }
}