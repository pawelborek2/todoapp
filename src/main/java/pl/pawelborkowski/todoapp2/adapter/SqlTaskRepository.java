package pl.pawelborkowski.todoapp2.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pawelborkowski.todoapp2.model.Task;
import pl.pawelborkowski.todoapp2.model.TaskRepository;

@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository <Task, Integer>{

    // Natywne SQL przyklad
    @Override
    @Query(nativeQuery = true,value = "select count(*) > 0 from tasks where id=:id")
    boolean existsById(@Param("id") Integer var1);

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);
}
