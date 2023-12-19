package com.github.specio.taskprocessor.taskapi.mapper;

import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import io.confluent.ksql.api.client.KsqlObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class TaskRequestMapper {
    public static KsqlObject taskToKsqlObject(UUID taskId, TaskParamsDto taskParamsDto) {
        return new KsqlObject()
                .put("id", taskId.toString())
                .put("task_id", taskId.toString())
                .put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date.from(Instant.now())))
                .put("input", taskParamsDto.input())
                .put("pattern", taskParamsDto.pattern());
    }

    public static KsqlObject taskToEmptyTaskKsqlObject(UUID taskId) {
        return new KsqlObject()
                .put("task_id", taskId.toString())
                .put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date.from(Instant.now())))
                .put("progress", 0);
    }
}
