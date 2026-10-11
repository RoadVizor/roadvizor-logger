package school.sptech.dto;

// guarda os dados do Jira de uma empresa, vindos da tabela empresa
public class EmpresaJiraDto {
    private String domainJira;
    private String emailJira;
    private String apiTokenJira;
    private String projectKey;

    public EmpresaJiraDto() {
    }

    public String getDomainJira() {
        return domainJira;
    }

    public void setDomainJira(String domainJira) {
        this.domainJira = domainJira;
    }

    public String getEmailJira() {
        return emailJira;
    }

    public void setEmailJira(String emailJira) {
        this.emailJira = emailJira;
    }

    public String getApiTokenJira() {
        return apiTokenJira;
    }

    public void setApiTokenJira(String apiTokenJira) {
        this.apiTokenJira = apiTokenJira;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

}
