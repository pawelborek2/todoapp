package pl.pawelborkowski.todoapp2.model;

import java.util.List;
import java.util.Optional;

public interface ProjectsRepository {
    List<Project> findAll();

    Optional<Project> findById(Integer id);

    Project save(Project project);

}
