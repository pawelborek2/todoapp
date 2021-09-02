package pl.pawelborkowski.todoapp2.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.pawelborkowski.todoapp2.TaskConfigurationProperties;
import pl.pawelborkowski.todoapp2.model.ProjectsRepository;
import pl.pawelborkowski.todoapp2.model.TaskGroupRepository;
import pl.pawelborkowski.todoapp2.model.TaskRepository;

@Configuration
public class LogicConfiguration {
    @Bean
    ProjectService projectService(
            final ProjectsRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskGroupService taskGroupService,
            final TaskConfigurationProperties config
    ) {
        return new ProjectService(repository, taskGroupRepository, config, taskGroupService);
    }
    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository repository,
            final TaskRepository taskRepository
    ) {
        return new TaskGroupService(repository,taskRepository);
    }
}
