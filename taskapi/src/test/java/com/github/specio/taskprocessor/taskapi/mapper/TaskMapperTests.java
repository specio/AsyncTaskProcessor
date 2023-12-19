package com.github.specio.taskprocessor.taskapi.mapper;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskResultDto;
import io.confluent.ksql.api.client.Row;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class TaskMapperTests {

    private final TaskMapper sut = Mappers.getMapper(TaskMapper.class);


    @Test
    void taskMappedToDto() {
        UUID id = UUID.randomUUID();
        int progress = 1;
        int position = 2;
        int typos = 3;
        Row input = Mockito.mock(Row.class);
        when(input.getString("TASK_ID")).thenReturn(id.toString());
        when(input.getInteger("PROGRESS")).thenReturn(progress);
        when(input.getInteger("OFFSET")).thenReturn(position);
        when(input.getInteger("TYPOS")).thenReturn(typos);

        TaskDto expectedOutput = new TaskDto(id, progress, new TaskResultDto(position, typos));
        TaskDto output = sut.taskToDTO(input);
        assertEquals(expectedOutput, output);
    }
}
