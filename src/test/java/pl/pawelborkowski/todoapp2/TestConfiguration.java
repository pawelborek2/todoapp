package pl.pawelborkowski.todoapp2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.pawelborkowski.todoapp2.model.Task;
import pl.pawelborkowski.todoapp2.model.TaskGroup;
import pl.pawelborkowski.todoapp2.model.TaskRepository;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;

@Configuration
public class TestConfiguration {
    @Bean
    @Primary
    @Profile("integration2")
    DataSource e2eTestDataSource() {
        var result = new DriverManagerDataSource("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=1","sa","");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

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
                int key = tasks.size() + 1;
                try {
                    var field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, key);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                tasks.put(key, entity);
                return tasks.get(key);
            }

            @Override
            public Page<Task> findAll(Pageable page) {
                return null;
            }

            @Override
            public List<Task> findByDone(boolean done) {
                return null;
            }

            @Override
            public List<Task> findAllByGroup_Id(Integer id) {
                return List.of();
            }
        };
    }
}
