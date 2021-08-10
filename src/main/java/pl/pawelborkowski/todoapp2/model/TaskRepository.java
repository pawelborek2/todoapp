package pl.pawelborkowski.todoapp2.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Optional<Task> findById(Integer id);

    boolean existsById(Integer var1);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    Task save(Task entity);

    Page<Task> findAll(Pageable page);

    // Znajdowanie tasków wykonanych lub nie
    List<Task> findByDone(@Param("state") boolean done);


}
