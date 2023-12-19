package com.github.specio.taskprocessor.taskapi.utils;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class TaskApiUtils {
    @Setter
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<TaskDto> getAllTasks() {
        ResponseEntity<TaskDto[]> responseEntity =
                restTemplate.getForEntity("http://localhost:" + port + "/api/v1/tasks", TaskDto[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        return Arrays.stream(responseEntity.getBody()).toList();
    }

    public UUID postTask(TaskParamsDto params) {
        HttpEntity<TaskParamsDto> requestEntity = new HttpEntity<>(params);
        ResponseEntity<UUID> uuidResponseEntity =
                restTemplate.postForEntity("http://localhost:" + port + "/api/v1/tasks", requestEntity, UUID.class);
        assertNotNull(uuidResponseEntity.getBody());
        assertInstanceOf(UUID.class, uuidResponseEntity.getBody());
        return uuidResponseEntity.getBody();
    }

    public TaskDto getTask(UUID uuid) {
        ResponseEntity<TaskDto> responseEntity =
                restTemplate.getForEntity("http://localhost:" + port + "/api/v1/tasks/" + uuid.toString(), TaskDto.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        return responseEntity.getBody();
    }
}
