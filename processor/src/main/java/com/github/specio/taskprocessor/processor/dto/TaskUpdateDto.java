package com.github.specio.taskprocessor.processor.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskUpdateDto {

    @JsonProperty(value = "TASK_ID")
    private UUID taskId;

    @JsonProperty(value = "TIMESTAMP")
    private String timestamp;

    @JsonProperty(value = "PROGRESS")
    private int progress;

    @JsonProperty(value = "OFFSET")
    private Integer offset;

    @JsonProperty(value = "TYPOS")
    private Integer typos;

}