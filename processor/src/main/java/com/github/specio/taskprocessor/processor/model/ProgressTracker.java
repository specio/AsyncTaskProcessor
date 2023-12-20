package com.github.specio.taskprocessor.processor.model;

import com.github.specio.taskprocessor.processor.dto.TaskProgressDto;
import com.github.specio.taskprocessor.processor.processor.ProgressSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ProgressTracker {
    private final ProgressSender progressSender;
    private final UUID taskId;
    private final int totalSteps;

    public void complete(int offset, int typos) {
        send(TaskProgressDto.createCompleted(taskId, offset, typos));
    }

    public void setCurrentProgress(int progressStep) {
        send(TaskProgressDto.createInProgress(taskId, calculateProgressPercent(progressStep)));
    }

    private int calculateProgressPercent(int progressStep) {
        return progressStep * 100 / totalSteps;
    }

    private void send(TaskProgressDto progressDto) {
        progressSender.sendProgressUpdate(progressDto);
    }
}
