package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import school.sptech.config.S3Client;
import school.sptech.dao.EmpresaDao;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {

        //isa->
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/roadvizor");
        String dbUser = System.getenv().getOrDefault("DB_USER", "user");
        String dbPass = System.getenv().getOrDefault("DB_PASSWORD", "senha");


        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPass);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        EmpresaDao empresaDao = new EmpresaDao(jdbcTemplate);

        String bucket = "datalake-gold-roadvizor-sptech";

        // cria o cliente da AWS uma vez só, usando a classe de config
        software.amazon.awssdk.services.s3.S3Client s3Client = S3Client.createClient();

        LeituraBucket leituraBucket = new LeituraBucket(s3Client);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // 3. Agenda a tarefa periódica (executa imediatamente e repete 5 min após o término)
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                System.out.println("Iniciando verificação no bucket S3...");
                List<String> arquivos = leituraBucket.listarArquivos(bucket, "client/");

                for (String key : arquivos) {
                    System.out.println("ARQUIVO: " + key);
                    System.out.println(leituraBucket.lerArquivo(bucket, key, empresaDao));
                }
            } catch (Exception e) {
                System.err.println("Erro durante o processamento agendado: " + e.getMessage());
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.MINUTES);

        // 4. Encerramento limpo quando o container/aplicação parar (SIGTERM / Ctrl+C)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Encerrando agendador e fechando cliente S3...");
            scheduler.shutdown();
            s3Client.close();
        }));
    }
}