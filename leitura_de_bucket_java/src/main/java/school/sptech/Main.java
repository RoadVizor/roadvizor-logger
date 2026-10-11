package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import school.sptech.dao.EmpresaDao;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        //isa->
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/roadvizor");
        dataSource.setUsername("user");
        dataSource.setPassword("senha");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        EmpresaDao empresaDao = new EmpresaDao(jdbcTemplate);

        String bucket = "nome_do_banco";

        LeituraBucket leituraBucket = new LeituraBucket();
        List<String> arquivos = leituraBucket.listarArquivos(bucket, "client/");

        // for para ler cada arquivo e abre chamado no Jira se houver alerta
        for (String key : arquivos) {
            System.out.println("ARQUIVO: " + key);
            System.out.println(leituraBucket.lerArquivo(bucket, key, empresaDao));
        }
    }
}