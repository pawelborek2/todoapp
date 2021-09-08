package pl.pawelborkowski.todoapp2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pawelborkowski.todoapp2.logic.TaskGroupService;
import pl.pawelborkowski.todoapp2.model.Task;
import pl.pawelborkowski.todoapp2.model.TaskGroup;
import pl.pawelborkowski.todoapp2.model.TaskGroupRepository;
import pl.pawelborkowski.todoapp2.model.TaskRepository;
import pl.pawelborkowski.todoapp2.model.projection.GroupReadModel;
import pl.pawelborkowski.todoapp2.model.projection.GroupWriteModel;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskRepository taskRepository;
    private final TaskGroupService service;

    public TaskGroupController(TaskRepository taskRepository, TaskGroupService service) {
        this.taskRepository = taskRepository;
        this.service = service;
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup (@RequestBody @Valid GroupWriteModel model) {
        GroupReadModel result = service.createGroup(model);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.info("Exposing all group");
        return ResponseEntity.ok(service.readAllGroup());
    }

    @GetMapping("{id}")
    ResponseEntity<List<Task>> readAllTaskFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @Transactional
    @PatchMapping ("/{id}")
    ResponseEntity<?> toggleGroup (@PathVariable int id) {
            service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument (IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
