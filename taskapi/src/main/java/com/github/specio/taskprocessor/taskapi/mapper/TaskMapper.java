package com.github.specio.taskprocessor.taskapi.mapper;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskResultDto;
import io.confluent.ksql.api.client.Row;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", expression = "java(UUID.fromString(row.getString(\"TASK_ID\")))")
    @Mapping(target = "progress", expression = "java(row.getInteger(\"PROGRESS\"))")
    @Mapping(target = "result", expression = "java(mapResult(row))")
    TaskDto taskToDTO(Row row);


    default TaskResultDto mapResult(Row row) {
        var offset = Optional.ofNullable(row.getInteger("OFFSET"));
        var typos = Optional.ofNullable(row.getInteger("TYPOS"));
        if(offset.isPresent() && typos.isPresent()){
            return new TaskResultDto(offset.get(), typos.get());
        }else{
            return null;
        }
    }
}
