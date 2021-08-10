package pl.pawelborkowski.todoapp2.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity //Encja
@Table(name ="tasks")
 public class Task  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; // Automatic generete id to task
    // mapowanie na pole mozna uzyc ale nie narazie
  //  @Column(name ="desccription")
    @NotBlank(message = "Task's description must not be empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    private Audit audit = new Audit();

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    public Task()
    {
    }
    public Task(String description, LocalDateTime deadline)
    {
        this.description= description;
        this.deadline= deadline;
    }

   // Geters and setters

    public int getId() { return id; }

    void setId(int id) { this.id = id; }

    public String getDescription() { return description; }

    void setDescription(String description) { this.description = description; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }


    public TaskGroup getGroup() {
        return group;
    }

    public void updateFrom(final Task source)  {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;
    }


}
