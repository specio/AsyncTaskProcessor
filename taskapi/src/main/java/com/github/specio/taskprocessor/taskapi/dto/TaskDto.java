package com.github.specio.taskprocessor.taskapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

public record TaskDto(UUID id,
                      @JsonInclude(JsonInclude.Include.NON_NULL) Integer progress,
                      @JsonInclude(JsonInclude.Include.NON_NULL) TaskResultDto result) {
}
