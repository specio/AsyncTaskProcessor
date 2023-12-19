package com.github.specio.taskprocessor.processor.mapper;

import com.github.specio.taskprocessor.processor.dto.TaskProgressDto;
import com.github.specio.taskprocessor.processor.model.ProgressTracker;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskProgressMapper {

    TaskProgressDto statusToTaskUpdate(ProgressTracker progressTracker);

}
