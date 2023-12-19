package com.github.specio.taskprocessor.taskapi;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import com.github.specio.taskprocessor.taskapi.service.TaskService;
import com.github.specio.taskprocessor.taskapi.utils.TaskApiUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = "dev.specio.taskprocessor.configuration")
class IntegrationTests {
    @LocalServerPort
    private int port;

    private TaskService taskService;
    private final TaskApiUtils utils = new TaskApiUtils();

    @BeforeEach
    void init() {
        utils.setPort(port);
    }

    @Test
    void createTaskShouldReturnUUID() {
        var receivedId = utils.postTask(new TaskParamsDto("CDQ", "ZAAABCDQ"));
        assertInstanceOf(UUID.class, receivedId);
    }

    @Test
    void createTaskShouldIncrementTotalTaskCount() throws InterruptedException {
        int initialAmount = utils.getAllTasks().size();
        utils.postTask(new TaskParamsDto("CDQ", "ABCDQ"));
        Thread.sleep(1000); //Wait for async task to propagate
        assertEquals(++initialAmount, utils.getAllTasks().size());
        utils.postTask(new TaskParamsDto("CDQ", "ABCDQ"));
        Thread.sleep(500); //Wait for async task to propagate
        assertEquals(++initialAmount, utils.getAllTasks().size());
        utils.postTask(new TaskParamsDto("CDQ", "ABCDQ"));
        Thread.sleep(500); //Wait for async task to propagate
        assertEquals(++initialAmount, utils.getAllTasks().size());
    }

    @Test
    void postAndGetTaskShouldReturnValidTaskDetails() throws InterruptedException {

        var id = utils.postTask(new TaskParamsDto("ABC", "ABCDQ"));
        Thread.sleep(500); //Wait for async task to propagate
        TaskDto taskDetails = utils.getTask(id);
        assertEquals(id, taskDetails.id());
    }
}
