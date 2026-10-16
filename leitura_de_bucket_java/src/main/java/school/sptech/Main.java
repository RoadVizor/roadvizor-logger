package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import software.amazon.awssdk.services.s3.S3Client;
import school.sptech.dao.EmpresaDao;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        //isa->
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            throw new IllegalStateException("defina DB_URL, DB_USER e DB_PASSWORD nas variáveis de ambiente");
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        EmpresaDao empresaDao = new EmpresaDao(jdbcTemplate);

        String bucket = "datalake-gold-roadvizor-sptech";

        // cria o cliente da AWS uma vez só, usando a classe de config
        software.amazon.awssdk.services.s3.S3Client s3Client = S3Client.create();

        LeituraBucket leituraBucket = new LeituraBucket(s3Client);
        List<String> arquivos = leituraBucket.listarArquivos(bucket, "client/");

        // for para ler cada arquivo e abre chamado no Jira se houver alerta
        for (String key : arquivos) {
            System.out.println("ARQUIVO: " + key);
            System.out.println(leituraBucket.lerArquivo(bucket, key, empresaDao));
        }
    }
}