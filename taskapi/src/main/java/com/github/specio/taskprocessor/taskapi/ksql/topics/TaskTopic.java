package com.github.specio.taskprocessor.taskapi.ksql.topics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskTopic {
    UPDATES("UPDATES"),
    QUEUE("TASKQUEUE"),
    TASKS("TASKS");

    private final String stringValue;
}
