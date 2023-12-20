package com.github.specio.taskprocessor.processor.processor;

import com.github.specio.taskprocessor.processor.dto.TaskProgressDto;
import com.github.specio.taskprocessor.processor.dto.TaskRequestDto;
import com.github.specio.taskprocessor.processor.model.ProgressTracker;
import com.github.specio.taskprocessor.processor.solver.PatternTypoSolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskProcessor {

    private final ProgressSender progressSender;

    @KafkaListener(topics = "taskqueue", groupId = "group_1")
    public void listenToTaskQueue(@Payload TaskRequestDto task) {
        logTaskProcessing(task);
        if (isValid(task))
            processTask(task);
        else
            failTask(task.taskId(), "Invalid task definition");
    }

    private void processTask(TaskRequestDto task) {
        final var progressTracker = new ProgressTracker(progressSender, task.taskId(), calculateTotalSteps(task));
        try {
            PatternTypoSolver.solveTask(progressTracker, task.input(), task.pattern());
        } catch (Exception e) {
            failTask(task.taskId(), e.getMessage());
        }
    }

    private boolean isValid(TaskRequestDto task) {
        return StringUtils.isNoneBlank(task.input(), task.pattern())
                && task.pattern().length() <= task.input().length();
    }

    private void failTask(UUID taskId, String failureReason) {
        logTaskFailure(taskId, failureReason);
        progressSender.sendProgressUpdate(TaskProgressDto.createFailed(taskId));
    }

    private static int calculateTotalSteps(TaskRequestDto task) {
        return task.input().length() * task.pattern().length();
    }

    private static void logTaskProcessing(TaskRequestDto task) {
        log.info("""
        PROCESSING {}:
            Input: {}
            Pattern: {}
        """, task.taskId(), task.input(), task.pattern());
    }

    private static void logTaskFailure(UUID taskId, String failureReason) {
        log.error("FAILED {}: {}", taskId, failureReason);
    }
}