package com.github.specio.taskprocessor.processor.mapper;

import com.github.specio.taskprocessor.processor.dto.TaskProgressDto;
import com.github.specio.taskprocessor.processor.model.ProgressTracker;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskProgressMapperTests {
    @MockBean
    private ProgressTracker progressTracker;
    private final TaskProgressMapper sut = Mappers.getMapper(TaskProgressMapper.class);

    @Test
    void statusToTaskUpdate() {
        UUID expectedUuid = UUID.randomUUID();
        when(progressTracker.getTaskId()).thenReturn(expectedUuid);
        when(progressTracker.getProgress()).thenReturn(1);
        when(progressTracker.getOffset()).thenReturn(2);
        when(progressTracker.getTypos()).thenReturn(3);
        when(progressTracker.getTimestamp()).thenReturn("Timestamp");

        TaskProgressDto expectedOutput = new TaskProgressDto(expectedUuid, "Timestamp", 1, 2, 3);
        TaskProgressDto output = sut.statusToTaskUpdate(progressTracker);
        assertEquals(expectedOutput, output);
    }
}
