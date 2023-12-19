package com.github.specio.taskprocessor.taskapi.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record TaskParamsDto(@NotNull String pattern,
                            @NotNull String input) {
    @AssertTrue(message = "Failed, invalid input parameters")
    private boolean isValid() {
        return input != null && pattern != null && pattern.length() <= input.length();
    }
}
