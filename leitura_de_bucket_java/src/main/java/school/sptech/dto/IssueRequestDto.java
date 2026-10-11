package school.sptech.dto;

// representa o JSON inteiro enviado ao Jira:
public class IssueRequestDto {
    private IssueFieldsDto fields;

    public IssueRequestDto() {
    }

    public IssueFieldsDto getFields() {
        return fields;
    }

    public void setFields(IssueFieldsDto fields) {
        this.fields = fields;
    }
}
