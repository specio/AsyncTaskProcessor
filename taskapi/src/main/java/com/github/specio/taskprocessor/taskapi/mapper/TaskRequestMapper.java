package com.github.specio.taskprocessor.taskapi.mapper;

import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import io.confluent.ksql.api.client.KsqlObject;
import org.apache.commons.lang3.NotImplementedException;

import java.util.UUID;

public class TaskRequestMapper {
    public static KsqlObject taskToKsqlObject(UUID taskId, TaskParamsDto taskParamsDto) {
        throw new NotImplementedException();
    }

    public static KsqlObject taskToEmptyTaskKsqlObject(UUID taskId) {
        throw new NotImplementedException();
    }
}
