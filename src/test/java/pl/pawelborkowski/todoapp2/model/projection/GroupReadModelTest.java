package pl.pawelborkowski.todoapp2.model.projection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.pawelborkowski.todoapp2.model.Task;
import pl.pawelborkowski.todoapp2.model.TaskGroup;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {
    @Test
    @DisplayName("should create null deadline for group when no task deadlines")
    void constructor_noDeadlines_createsNullDeadline() {
        //given
        var source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar",null)));

        //when
        var result = new GroupReadModel(source);

        //then

        assertThat(result).hasFieldOrPropertyWithValue("deadline",null);
    }
}