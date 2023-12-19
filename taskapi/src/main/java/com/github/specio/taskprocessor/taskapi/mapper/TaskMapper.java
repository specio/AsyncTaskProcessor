package com.github.specio.taskprocessor.taskapi.mapper;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskResultDto;
import io.confluent.ksql.api.client.Row;
import org.apache.commons.lang3.NotImplementedException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto taskToDTO(Row row);


    default TaskResultDto mapResult(Row row) {
        throw new NotImplementedException();
    }
}
