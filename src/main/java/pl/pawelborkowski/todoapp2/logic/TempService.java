package pl.pawelborkowski.todoapp2.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pawelborkowski.todoapp2.model.Task;
import pl.pawelborkowski.todoapp2.model.TaskGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TempService {
    @Autowired
    List<String> temp(TaskGroupRepository repository)
    {
        return repository.findAll().stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream())
                .map(Task::getDescription)
                .collect(Collectors.toList());
    }
}
