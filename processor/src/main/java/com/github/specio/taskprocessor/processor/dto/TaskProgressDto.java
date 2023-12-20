package com.github.specio.taskprocessor.processor.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)
public record TaskProgressDto(UUID taskId,
                              String timestamp,
                              Integer progress,
                              Integer offset,
                              Integer typos) {

    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static TaskProgressDto createInProgress(UUID taskId, int progress) {
        return new TaskProgressDto(taskId, createTimestamp(), progress, null, null);
    }

    public static TaskProgressDto createFailed(UUID taskId) {
        return TaskProgressDto.createCompleted(taskId, -1, -1);
    }

    public static TaskProgressDto createCompleted(UUID taskId, int offset, int typos) {
        return new TaskProgressDto(taskId, createTimestamp(), 100, offset, typos);
    }

    private static String createTimestamp() {
        return TIMESTAMP_FORMAT.format(Date.from(Instant.now()));
    }
}