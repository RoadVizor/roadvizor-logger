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

    private final S3Client s3Client;

    public LeituraBucket(S3Client s3Client) {
        this.s3Client = s3Client;
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

            // dados extras do servidor para detalhar o chamado (campos opcionais: se não vierem, usa "-")
            String apelido = textoOuPadrao(raiz, "apelido");
            String empresa = textoOuPadrao(raiz, "empresa");
            String localizacaoKm = textoOuPadrao(raiz, "localizacao_km");
            String sentido = textoOuPadrao(raiz, "sentido");
            String timestamp = textoOuPadrao(raiz, "timestamp");

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

                // limite máximo da métrica (opcional)
                double limiteMax = metrica.has("limite_max") && !metrica.get("limite_max").isNull()
                        ? metrica.get("limite_max").asDouble()
                        : 0.0;

                System.out.println(
                        "Componente: " + componente +
                                " | Valor medido: " + valor +
                                " " + unidade +
                                " | Alerta: " + alerta);

                // validação do alerta
                if (alerta) {

                    // titulo informativo
                    String titulo = "🚨 ALERTA: Componente [%s] em nível crítico no servidor %s"
                            .formatted(componente, apelido);

                    // descrição mais detalhada sobre a situação do equipamento
                    String descricao = """

                            🖥️ SERVIDOR : %s (MAC: %s)
                            🏢 Empresa  : %s
                            📍 Local    : %s (%s) | 🕒 Horário: %s
                            -------------------------------------------------------------
                            📊 Componente: %s
                            📈 Valor     : %.2f %s (Limite: %.2f)
                            =============================================================
                            """.formatted(
                            apelido,
                            mac,
                            empresa,
                            localizacaoKm,
                            sentido,
                            timestamp,
                            componente,
                            valor,
                            unidade != null ? unidade : "",
                            limiteMax
                    );

                    String resposta = jira.createIssue(config.getProjectKey(), titulo, descricao, "10010");
                    System.out.println("🚀 Chamado aberto com sucesso! Resposta do Jira:\n" + resposta);
                }
            }

        } catch (JsonProcessingException e) {
            // se o arquivo vier quebrado, avisa e segue em frente sem derrubar o programa
            System.out.println("Erro ao interpretar o JSON.");
            System.out.println(e.getMessage());
        }

        return conteudo;
    }

    // lê um campo de texto do JSON; se não existir ou for nulo, devolve "-"
    private String textoOuPadrao(JsonNode no, String campo) {
        return no.has(campo) && !no.get(campo).isNull() ? no.get(campo).asText() : "-";
    }
}