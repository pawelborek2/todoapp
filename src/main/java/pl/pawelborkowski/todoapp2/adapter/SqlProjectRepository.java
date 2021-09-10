package pl.pawelborkowski.todoapp2.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pawelborkowski.todoapp2.model.Project;
import pl.pawelborkowski.todoapp2.model.ProjectsRepository;

import java.util.List;

public interface SqlProjectRepository extends ProjectsRepository, JpaRepository<Project,Integer> {

    @Override
    @Query("from Project p join fetch p.steps ")
    List<Project> findAll();
}
