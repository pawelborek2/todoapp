package pl.pawelborkowski.todoapp2.logic;

import org.springframework.beans.factory.BeanCreationNotAllowedException;
import org.springframework.stereotype.Service;
import pl.pawelborkowski.todoapp2.TaskConfigurationProperties;
import pl.pawelborkowski.todoapp2.model.*;
import pl.pawelborkowski.todoapp2.model.projection.GroupReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectsRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties properties;

    public ProjectService(ProjectsRepository repository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties properties) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.properties = properties;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save (final Project toSave){
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline)
    {
        if(taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId) && !properties.template.isAllowMultipleTasks() ) {
                throw new IllegalStateException("Only one undone group from project is allowed");
        }

        TaskGroup source = repository.findById(projectId)
                .map(project -> {
                    var result = new TaskGroup();
                    result.setDescription(project.getDescription());
                    result.setDone(false);
                    result.setProject(project);
                    result.setTasks(
                            project.getProjectSteps().stream()
                                    .map(step -> new Task
                                            (step.getDescription(),
                                            deadline.plusDays(step.getDaysToDeadline())))
                                    .collect(Collectors.toSet()));
                    return taskGroupRepository.save(result);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));

        return new GroupReadModel(source);
    }
}
