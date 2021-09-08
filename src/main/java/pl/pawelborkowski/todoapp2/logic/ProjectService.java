package pl.pawelborkowski.todoapp2.logic;

import pl.pawelborkowski.todoapp2.TaskConfigurationProperties;
import pl.pawelborkowski.todoapp2.model.*;
import pl.pawelborkowski.todoapp2.model.projection.GroupReadModel;
import pl.pawelborkowski.todoapp2.model.projection.GroupTaskWriteModel;
import pl.pawelborkowski.todoapp2.model.projection.GroupWriteModel;
import pl.pawelborkowski.todoapp2.model.projection.ProjectWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class ProjectService {
    private ProjectsRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties properties;
    private TaskGroupService taskGroupService;

    public ProjectService(ProjectsRepository repository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties properties, TaskGroupService taskGroupService) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.properties = properties;
        this.taskGroupService = taskGroupService;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save (final ProjectWriteModel toSave){
        return repository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline)
    {
        if(taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId) && !properties.template.isAllowMultipleTasks() ) {
                throw new IllegalStateException("Only one undone group from project is allowed");
        }

        return repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getProjectSteps().stream()
                                    .map(projectSteps -> {
                                                var task = new GroupTaskWriteModel();
                                                task.setDescription(projectSteps.getDescription());
                                                task.setDeadline(deadline.plusDays(projectSteps.getDaysToDeadline()));
                                                return task;
                                    }
                                    ).collect(Collectors.toSet())
                                    );
                     return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
