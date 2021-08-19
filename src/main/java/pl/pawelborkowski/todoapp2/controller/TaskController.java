package pl.pawelborkowski.todoapp2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pawelborkowski.todoapp2.model.Task;
import pl.pawelborkowski.todoapp2.model.TaskRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

   @GetMapping (value = "/tasks", params ={"!sort","!page","!size"} )
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all the task");
        return  ResponseEntity.ok(repository.findAll());
    }

    @GetMapping ("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return  ResponseEntity.ok(repository.findAll(page).getContent());
    }
    @PutMapping  ("/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody Task toUpdate) {
        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
       repository.findById(id)
               .ifPresent(task -> {
                       task.updateFrom(toUpdate);
                       repository.save(task);
                   });

        return ResponseEntity.noContent().build();
    }

    @GetMapping ("/tasks/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping ("/tasks")
    ResponseEntity<Task> createTask (@RequestBody  @Valid Task toCreate){
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    // Tranzakcja zmmieniająca stan tasku
    @Transactional
    @PatchMapping  ("/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }
}
