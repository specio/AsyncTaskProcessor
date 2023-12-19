package com.github.specio.taskprocessor.taskapi.service;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    public TaskDto getTask(UUID taskId) throws ExecutionException, InterruptedException, TimeoutException {
        throw new NotImplementedException();
    }

    public List<TaskDto> getAllTasks() {
        throw new NotImplementedException();
    }

    public UUID scheduleTask(TaskParamsDto taskParamsDto) {
        throw new NotImplementedException();
    }
}
