package pl.pawelborkowski.todoapp2.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.pawelborkowski.todoapp2.TaskConfigurationProperties;
import pl.pawelborkowski.todoapp2.model.*;
import pl.pawelborkowski.todoapp2.model.projection.GroupReadModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just one group and the other undone group exists ")
    void createGroup_noMultipleGroupsConfig_And_undoneGroup_throwsIllegalStateException() {
        // given - prepare data to test
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        // and
        TaskConfigurationProperties mocConfig = configurationReturning(false);

        // system under test
        var toTest = new ProjectService(null, mockGroupRepository,mocConfig, null);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(0,LocalDateTime.now()));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");


    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured is ok and no project for given id ")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {
        // given - prepare data to test
        var mockGroupRepository = mock(TaskGroupRepository.class);
        //and
        TaskConfigurationProperties mocConfig = configurationReturning(true);
        // and
        var mockRepository = mock(ProjectsRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // system under test
        var toTest = new ProjectService(mockRepository, mockGroupRepository,mocConfig, null);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(0,LocalDateTime.now()));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and no project for given id ")
    void createGroup_noMultipleGroupConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        // given - prepare data to test
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        // and
        TaskConfigurationProperties mocConfig = configurationReturning(true);
        // and
        var mockRepository = mock(ProjectsRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // system under test
        var toTest = new ProjectService(mockRepository, mockGroupRepository,mocConfig, null);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(0,LocalDateTime.now()));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }
    @Test
    @DisplayName("should create new group from project")
    void createGroup_configurationOk_existingProject_createAndSavesGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        //
        var project =  projectWith("bar",Set.of(-1L, -2L));
        var mockRepository = mock(ProjectsRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        //and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceWithInMemRepo = dummyGroupService(inMemoryGroupRepo);
        int countBeforeCall = inMemoryGroupRepo.count();
        //and
        TaskConfigurationProperties mocConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo,mocConfig, serviceWithInMemRepo);

        //when
        GroupReadModel result = toTest.createGroup(1 , today);
        //then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks().stream().allMatch(task -> task.getDescription().equals("foo"))).isTrue();
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count() );

    }

    private TaskGroupService dummyGroupService(InMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo, null);
    }

    private Project projectWith(String projectDescription, Set<Long> daysToDeadLine) {
        Set<ProjectSteps> steps = daysToDeadLine.stream()
                .map(days -> {
                    var step = mock(ProjectSteps.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getProjectSteps()).thenReturn(steps);
        return result;
    }


    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        // and
        var mocConfig = mock(TaskConfigurationProperties.class);
        mocConfig.template=mockTemplate;
        return mocConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository() ;
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() ==0){
                try {
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(taskGroup -> !taskGroup.isDone())
                    .anyMatch(taskGroup -> taskGroup.getProject() != null && taskGroup.getProject().getId() == projectId);
        }
    };
}