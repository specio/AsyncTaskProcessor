package com.github.specio.taskprocessor.processor.mapper;

import com.github.specio.taskprocessor.processor.dto.TaskUpdateDto;
import com.github.specio.taskprocessor.processor.mapper.StatusMapper;
import com.github.specio.taskprocessor.processor.model.StatusReporter;
import com.github.specio.taskprocessor.processor.solver.TaskParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;


@SpringBootTest
class StatusMapperTests {

    @MockBean
    private StatusReporter statusReporter;
    private final StatusMapper sut = Mappers.getMapper(StatusMapper.class);


    @Test
    void statusToTaskUpdate() {
        UUID expectedUuid = UUID.randomUUID();
        when(statusReporter.getTaskId()).thenReturn(expectedUuid);
        when(statusReporter.getProgress()).thenReturn(1);
        when(statusReporter.getOffset()).thenReturn(2);
        when(statusReporter.getTypos()).thenReturn(3);
        when(statusReporter.getTimestamp()).thenReturn("Timestamp");

        TaskUpdateDto expectedOutput = new TaskUpdateDto(expectedUuid, "Timestamp", 1, 2, 3);
        TaskUpdateDto output = sut.statusToTaskUpdate(statusReporter);
        assertEquals(expectedOutput, output);
    }
}
