package school.sptech.dto;

// representa esse objeto { "name": "Tarefa" }
public class IssueTypeDto {
    private String name;

    public IssueTypeDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
