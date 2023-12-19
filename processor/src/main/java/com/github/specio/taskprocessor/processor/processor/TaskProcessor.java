package com.github.specio.taskprocessor.processor.processor;

import com.github.specio.taskprocessor.processor.dto.TaskProgressDto;
import com.github.specio.taskprocessor.processor.dto.TaskRequestDto;
import com.github.specio.taskprocessor.processor.exception.InvalidInputDataException;
import com.github.specio.taskprocessor.processor.mapper.TaskProgressMapper;
import com.github.specio.taskprocessor.processor.model.ProgressTracker;
import com.github.specio.taskprocessor.processor.solver.PatternTypoSolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskProcessor {
    private final KafkaTemplate<String, TaskProgressDto> kafkaTemplate;
    private final TaskProgressMapper taskProgressMapper;

    @KafkaListener(topics = "taskqueue", groupId = "group_1")
    public void listenToTaskQueue(@Payload TaskRequestDto message,
                                  @Header(KafkaHeaders.RECEIVED_KEY) UUID key) {

        final String input = message.getInput();
        final String pattern = message.getPattern();
        log.info("""
        Processing {},
        Input: {},
        Pattern: {}
        """, key, input, pattern);
        ProgressTracker progressTracker = new ProgressTracker(kafkaTemplate, taskProgressMapper, message.getTaskId());
        try {
            PatternTypoSolver.verifyInputData(input, pattern);
            int totalSteps = input.length() * pattern.length();
            progressTracker.setTotalSteps(totalSteps);
            PatternTypoSolver.solveTask(progressTracker, message.getInput(), message.getPattern());
        } catch (InvalidInputDataException | InterruptedException e) {
            progressTracker.failed(e.getMessage());
        }
    }
}