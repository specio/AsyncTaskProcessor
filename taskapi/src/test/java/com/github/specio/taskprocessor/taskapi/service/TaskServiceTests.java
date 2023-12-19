package com.github.specio.taskprocessor.taskapi.service;

import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import com.github.specio.taskprocessor.taskapi.ksql.KsqlConnector;
import com.github.specio.taskprocessor.taskapi.ksql.TaskTopic;
import com.github.specio.taskprocessor.taskapi.utils.TaskApiUtils;
import io.confluent.ksql.api.client.KsqlObject;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskServiceTests {
    @LocalServerPort
    private int port;

    @Autowired
    TaskService taskService;
    @MockBean
    private KsqlConnector connector;

    private final TaskApiUtils utils = new TaskApiUtils();

    @BeforeEach
    void init() {
        utils.setPort(port);
    }

    @Test
    void scheduleTaskShouldReturnUUID() {
        CompletableFuture<Void> dummy = Mockito.mock(CompletableFuture.class);
        when(connector.insert(eq(TaskTopic.UPDATES), any(KsqlObject.class))).thenReturn(dummy);
        when(connector.insert(eq(TaskTopic.QUEUE), any(KsqlObject.class))).thenReturn(dummy);
        UUID receivedId = taskService.scheduleTask(new TaskParamsDto("CDQ", "ABCDQ"));
        assertInstanceOf(UUID.class, receivedId);
    }

//    @Test
//    void getAllTasksShouldReturnAllExpectedUUIDs() {
//        List<UUID> expectedIds = IntStream.rangeClosed(1, 10)
//                        .mapToObj((i) -> UUID.randomUUID()).toList();
//
//        when(taskManager.getAllTaskIds()).thenReturn(expectedIds.stream());
//
//        var returnedIds = taskService.getAllTasks();
//        assertEquals(expectedIds.size(), returnedIds.count());
//    }
//
//    @Test
//    void getTaskShouldReturnValidTaskDTO() {
//        var params = new TaskParams("CDQ", "ABCDQ");
//        Task task = Task.from(params);
//        var expectedId = task.getId();
//        when(taskManager.getTask(expectedId)).thenReturn(Optional.of(task));
//
//        var returnedTask = taskService.getTask(expectedId);
//        assertInstanceOf(TaskDTO.class, returnedTask);
//    }
}
