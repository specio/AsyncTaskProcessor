package com.github.specio.taskprocessor.taskapi.mapper;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import com.github.specio.taskprocessor.taskapi.mapper.TaskMapper;
import com.github.specio.taskprocessor.taskapi.mapper.TaskRequestMapper;
import io.confluent.ksql.api.client.KsqlObject;
import io.confluent.ksql.api.client.Row;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;



class TaskRequestMapperTests {
    @ParameterizedTest
    @MethodSource
    void taskMappedToKsqlObject(TaskParamsDto params) {
        UUID taskId = UUID.randomUUID();

        //TaskDto expectedOutput = new TaskDto(input.getId(), input.getState(), input.getProgress(), input.getResult());
        KsqlObject actual = TaskRequestMapper.taskToKsqlObject(taskId,params);

        KsqlObject expected = new KsqlObject()
                .put("id", taskId.toString())
                .put("task_id", taskId.toString())
                .put("timestamp", actual.getValue("timestamp"))
                .put("input", params.input())
                .put("pattern", params.pattern());

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> taskMappedToKsqlObject() {
        return Stream.of(
                arguments(new TaskParamsDto("BCD", "ABCD")),
                arguments(new TaskParamsDto("", ""))
        );
    }
}
