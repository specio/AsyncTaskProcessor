package com.github.specio.taskprocessor.taskapi.controller;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import com.github.specio.taskprocessor.taskapi.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "${api.task-api.get.description}",
            description = "${api.task-api.get.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}",
                    content = @Content(examples =
                    @ExampleObject("Invalid input data."),
                            mediaType = "text/plain;charset=UTF-8")),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto getTask(@PathVariable("id") UUID id) throws InterruptedException, ExecutionException, TimeoutException {
        return taskService.getTask(id);
    }

    @Operation(
            summary = "${api.task-api.get-all.description}",
            description = "${api.task-api.get-all.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasks() {
        return taskService.getAllTasks();
    }

    @Operation(
            summary = "${api.task-api.post.description}",
            description = "${api.task-api.post.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}",
                    content = @Content(examples =
                    @ExampleObject("{type = about:blank, " +
                            "title = Bad Request, " +
                            "status = 400, " +
                            "detail = Invalid request content., " +
                            "instance = /api/v1/task}"),
                            mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UUID scheduleTask(@Valid @RequestBody TaskParamsDto taskParamsDto) {
        return taskService.scheduleTask(taskParamsDto);
    }

}