package school.sptech.config;
import school.sptech.dto.ProjectDto;
import school.sptech.dto.IssueTypeDto;
import school.sptech.dto.IssueFieldsDto;
import school.sptech.dto.IssueRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpResponse;

//classe que conversa com o jira
public class Jira {
    private String email;
    private String baseURl;
    private String apiToken;

    public Jira(String email, String baseURl, String apiToken) {
        this.email = email;
        this.baseURl = baseURl;
        this.apiToken = apiToken;
    }

    public String createIssue(String projectKey, String summary, String description, String issueType) throws Exception {
        ProjectDto project = new ProjectDto();
        project.setKey(projectKey);

        IssueTypeDto type = new IssueTypeDto();
        type.setId(issueType);

        IssueFieldsDto fields = new IssueFieldsDto();
        fields.setProject(project);
        fields.setSummary(summary);
        fields.setDescription(description);
        fields.setIssuetype(type);
        IssueRequestDto request = new IssueRequestDto();
        request.setFields(fields);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        // próxima parte: montar e enviar a requisição HTTP com esse json
        String credenciais = email + ":" + apiToken;
        String auth = Base64.getEncoder().encodeToString(credenciais.getBytes());
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(baseURl + "/rest/api/2/issue"))
                .header("Authorization", "Basic " + auth)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request2, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            throw new Exception("Erro ao criar issue: " + response.statusCode() + " - " + response.body());
        }
    }
}
