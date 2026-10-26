package school.sptech.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.dto.EmpresaJiraDto;

// represta a conexão com o banco
public class EmpresaDao {
    private JdbcTemplate jdbcTemplate;

    public EmpresaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public EmpresaJiraDto buscarConfigPorMac(String mac) {

        String sql = "SELECT e.domain_jira, e.email_jira, e.api_token_jira, e.project_key" +
                " FROM servidor s JOIN empresa e ON s.empresa_id = e.id_empresa WHERE s.endereco_mac = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(EmpresaJiraDto.class), mac);

    }

}
