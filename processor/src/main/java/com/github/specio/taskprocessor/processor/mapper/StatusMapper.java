package com.github.specio.taskprocessor.processor.mapper;

import com.github.specio.taskprocessor.processor.dto.TaskUpdateDto;
import com.github.specio.taskprocessor.processor.model.StatusReporter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusMapper {

    TaskUpdateDto statusToTaskUpdate(StatusReporter statusReporter);

}
