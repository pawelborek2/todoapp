package pl.pawelborkowski.todoapp2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.pawelborkowski.todoapp2.logic.TaskGroupService;
import pl.pawelborkowski.todoapp2.model.ProjectSteps;
import pl.pawelborkowski.todoapp2.model.Task;
import pl.pawelborkowski.todoapp2.model.TaskRepository;
import pl.pawelborkowski.todoapp2.model.projection.GroupReadModel;
import pl.pawelborkowski.todoapp2.model.projection.GroupTaskWriteModel;
import pl.pawelborkowski.todoapp2.model.projection.GroupWriteModel;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskRepository taskRepository;
    private final TaskGroupService service;

    public TaskGroupController(TaskRepository taskRepository, TaskGroupService service) {
        this.taskRepository = taskRepository;
        this.service = service;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String showGroups(Model model) {
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }
    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(
            @ModelAttribute("group") @Valid GroupWriteModel current,
            BindingResult bindingResult,
            Model model
    ) {
        if(bindingResult.hasErrors()) {
            return "groups";
        }
        service.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Dodano grupę");
        return "groups";
    }

    @PostMapping(params = "addTask")
    String addGroupsTask(@ModelAttribute("group") GroupWriteModel current) {
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }



    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupReadModel> createGroup (@RequestBody @Valid GroupWriteModel model) {
        GroupReadModel result = service.createGroup(model);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }


    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.info("Exposing all group");
        return ResponseEntity.ok(service.readAllGroup());
    }

    @ResponseBody
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> readAllTaskFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @ResponseBody
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

    @ModelAttribute("groups")
    List<GroupReadModel> getGroups() {
        return service.readAllGroup();
    }
}
