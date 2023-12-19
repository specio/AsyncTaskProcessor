package com.github.specio.taskprocessor.taskapi.controller;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import com.github.specio.taskprocessor.taskapi.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto getTask(@PathVariable("id") UUID id) throws InterruptedException, ExecutionException, TimeoutException {
        return taskService.getTask(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UUID scheduleTask(@Valid @RequestBody TaskParamsDto taskParamsDto) {
        return taskService.scheduleTask(taskParamsDto);
    }

}