package pl.pawelborkowski.todoapp2.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name ="project_steps")
public class ProjectSteps {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; // Automatic generate id
    @NotBlank(message = "Project step's description must not be empty")
    private String description;
    @NotNull(message = "Days to deadline must not be null")
    private long daysToDeadline;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Getters and setters

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDaysToDeadline() {
        return daysToDeadline;
    }

    public void setDaysToDeadline(long daysToDeadline) {
        this.daysToDeadline = daysToDeadline;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
