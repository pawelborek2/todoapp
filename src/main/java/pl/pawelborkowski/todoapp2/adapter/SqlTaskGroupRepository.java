package pl.pawelborkowski.todoapp2.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pawelborkowski.todoapp2.model.TaskGroup;
import pl.pawelborkowski.todoapp2.model.TaskGroupRepository;

import java.util.List;

@Repository
public interface SqlTaskGroupRepository extends TaskGroupRepository,JpaRepository<TaskGroup, Integer> {

    @Override
    @Query("from TaskGroup g join fetch g.tasks ")
    List<TaskGroup> findAll();

    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}
