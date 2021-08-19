package pl.pawelborkowski.todoapp2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.pawelborkowski.todoapp2.model.Task;
import pl.pawelborkowski.todoapp2.model.TaskRepository;

import java.util.*;

@Configuration
public class TestConfiguration {
    @Bean
    @Primary
    @Profile("integration")
    TaskRepository testRepo() {
        return new TaskRepository() {
            private Map<Integer, Task> tasks = new HashMap<>();

            @Override
            public List<Task> findAll() {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Optional<Task> findById(Integer id) {
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public boolean existsById(Integer var1) {
                return tasks.containsKey(var1);
            }

            @Override
            public boolean existsByDoneIsFalseAndGroup_Id(Integer groupId) {
                return false;
            }

            @Override
            public Task save(Task entity) {
                return tasks.put(tasks.size() + 1, entity);
            }

            @Override
            public Page<Task> findAll(Pageable page) {
                return null;
            }

            @Override
            public List<Task> findByDone(boolean done) {
                return null;
            }
        };
    }
}
