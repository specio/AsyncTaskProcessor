package com.github.specio.taskprocessor.taskapi.service;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskResultDto;
import com.github.specio.taskprocessor.taskapi.ksql.KsqlConnector;
import com.github.specio.taskprocessor.taskapi.ksql.TaskTopic;
import com.github.specio.taskprocessor.taskapi.utils.TaskApiUtils;
import io.confluent.ksql.api.client.KsqlObject;
import io.confluent.ksql.api.client.Row;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void getAllTasksShouldReturnAllExpectedUUIDs() {
        UUID id = UUID.randomUUID();
        int progress = 5;
        int position = 6;
        int typos = 7;
        Row input = Mockito.mock(Row.class);
        when(input.getString("TASK_ID")).thenReturn(id.toString());
        when(input.getInteger("PROGRESS")).thenReturn(progress);
        when(input.getInteger("OFFSET")).thenReturn(position);
        when(input.getInteger("TYPOS")).thenReturn(typos);
        List<Row> expected = List.of(input);

        when(connector.getTasks()).thenReturn(expected);

        List<TaskDto> returnedIds = taskService.getAllTasks();
        assertEquals(new TaskDto(id,progress,new TaskResultDto(position,typos)), returnedIds.get(0));
    }
}
