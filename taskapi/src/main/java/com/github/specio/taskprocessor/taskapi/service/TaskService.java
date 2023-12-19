package com.github.specio.taskprocessor.taskapi.service;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import com.github.specio.taskprocessor.taskapi.exception.TaskNotFoundException;
import com.github.specio.taskprocessor.taskapi.ksql.KsqlConnector;
import com.github.specio.taskprocessor.taskapi.ksql.TaskTopic;
import com.github.specio.taskprocessor.taskapi.mapper.TaskMapper;
import com.github.specio.taskprocessor.taskapi.mapper.TaskRequestMapper;
import io.confluent.ksql.api.client.KsqlObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskMapper taskMapper;
    private final KsqlConnector connector;

    @Cacheable(value = "completedTask", unless = "#result.progress < 100")
    public TaskDto getTask(UUID taskId) throws ExecutionException, InterruptedException, TimeoutException {
        log.info("Getting value for task id: {} is not cached yet", taskId);
        return taskMapper.taskToDTO(connector.getTask(taskId).orElseThrow(TaskNotFoundException::new));
    }

    public List<TaskDto> getAllTasks() {
        return connector.getTasks().stream()
                .map(taskMapper::taskToDTO).toList();
    }

    public UUID scheduleTask(TaskParamsDto taskParamsDto) {
        UUID taskId = UUID.randomUUID();
        KsqlObject taskRequest = TaskRequestMapper.taskToKsqlObject(taskId, taskParamsDto);
        KsqlObject emptyTask = TaskRequestMapper.taskToEmptyTaskKsqlObject(taskId);

        connector.insert(TaskTopic.QUEUE, taskRequest);
        connector.insert(TaskTopic.UPDATES, emptyTask);
        return taskId;
    }
}
