package pl.pawelborkowski.todoapp2.logic;

import pl.pawelborkowski.todoapp2.model.Project;
import pl.pawelborkowski.todoapp2.model.TaskGroup;
import pl.pawelborkowski.todoapp2.model.TaskGroupRepository;
import pl.pawelborkowski.todoapp2.model.TaskRepository;
import pl.pawelborkowski.todoapp2.model.projection.GroupReadModel;
import pl.pawelborkowski.todoapp2.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;


public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository repository,final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }
    public GroupReadModel createGroup(final GroupWriteModel source){
        return createGroup(source,null);
    }

    public GroupReadModel createGroup(GroupWriteModel source, Project project) {
        TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }


    public List<GroupReadModel> readAllGroup() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        }
        TaskGroup result = repository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }


}
