package pl.pawelborkowski.todoapp2.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(Task entity);

    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}

