package pl.pawelborkowski.todoapp2.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name ="projects")
 public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Project's description must not be empty")
    private String description;

    // nie ma cascade ponieważ gdy usuwamy projekt to nie usuwamy grup taskow
    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> tasksGroups;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectSteps> steps;

    public Project()
    {
    }

   // Getters and setters

    public int getId() { return id; }

    void setId(int id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Set<TaskGroup> getTasksGroups() {
        return tasksGroups;
    }

    public void setTasksGroups(Set<TaskGroup> tasksGroups) {
        this.tasksGroups = tasksGroups;
    }

    public Set<ProjectSteps> getSteps() {
        return steps;
    }

    public void setSteps(Set<ProjectSteps> projectSteps) {
        this.steps = projectSteps;
    }

}
