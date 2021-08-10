package pl.pawelborkowski.todoapp2.model.projection;

import pl.pawelborkowski.todoapp2.model.Task;

public class GroupTaskReadModel {
    private String description;
    private boolean done;

    public String getDescription() {
        return description;
    }

    public GroupTaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
