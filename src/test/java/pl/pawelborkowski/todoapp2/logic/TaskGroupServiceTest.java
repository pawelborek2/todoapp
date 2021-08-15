package pl.pawelborkowski.todoapp2.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.pawelborkowski.todoapp2.model.TaskGroup;
import pl.pawelborkowski.todoapp2.model.TaskGroupRepository;
import pl.pawelborkowski.todoapp2.model.TaskRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when group has undone task's ")
    void toggleGroup_undoneTasks_throw_IllegalStateException() {
        //given
        TaskRepository mockTaskRepository = TaskRepositoryReturning(true);
        // system under test
        var toTest = new TaskGroupService(null,mockTaskRepository);
        // catch exception
        var exception = catchThrowable(() -> toTest.toggleGroup(0));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has undone tasks");
    }


    @Test
    @DisplayName("should throw IllegalArgumentException when TaskGroup with given id not found ")
    void toggleGroup_noTaskGroup_throw_IllegalStateException() {
        //given
        TaskRepository mockTaskRepository = TaskRepositoryReturning(false);
        //and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        // system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository,mockTaskRepository);
        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");
    }

    @Test
    @DisplayName("should change status for TaskGroup with finished tasks")
    void toggleGroup_worksAsExpected() {
        //given
        TaskRepository mockTaskRepository = TaskRepositoryReturning(false);
        //and
        var taskGroup = new TaskGroup();
        var beforeToggle = taskGroup.isDone();
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(taskGroup));
        // system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository,mockTaskRepository);
        // when
        toTest.toggleGroup(0);
        //then
        assertThat(taskGroup.isDone()).isEqualTo(!beforeToggle);

    }

    private TaskRepository TaskRepositoryReturning(boolean result) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }

}