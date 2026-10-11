package school.sptech.dto;
import com.fasterxml.jackson.databind.JsonNode;

// representa o conteúdo dos campos = fields"
public class IssueFieldsDto {
    private ProjectDto project;
    private String summary;
    private IssueTypeDto issuetype;
    private String description;

    public IssueFieldsDto() {
    }

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public IssueTypeDto getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(IssueTypeDto issuetype) {
        this.issuetype = issuetype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}