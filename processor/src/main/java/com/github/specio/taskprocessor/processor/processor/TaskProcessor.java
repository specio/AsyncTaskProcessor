package com.github.specio.taskprocessor.processor.processor;

import com.github.specio.taskprocessor.processor.dto.StatusDto;
import com.github.specio.taskprocessor.processor.dto.TaskRequestDto;
import com.github.specio.taskprocessor.processor.dto.TaskUpdateDto;
import com.github.specio.taskprocessor.processor.exception.InvalidInputDataException;
import com.github.specio.taskprocessor.processor.mapper.StatusMapper;
import com.github.specio.taskprocessor.processor.model.StatusReporter;
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
    private final KafkaTemplate<String, TaskUpdateDto> kafkaTemplate;
    private final StatusMapper statusMapper;

    @KafkaListener(topics = "taskqueue", groupId = "group_1")
    public void listenToTaskQueue(@Payload TaskRequestDto message,
                                  @Header(KafkaHeaders.RECEIVED_KEY) UUID key) {

        String input = message.getInput();
        String pattern = message.getPattern();
        log.info("Processing {}", key);
        log.info("Input: {}", input);
        log.info("Pattern: {}", pattern);
        StatusReporter statusReporter =  new StatusReporter(kafkaTemplate, statusMapper, message.getTaskId());
        try {
            PatternTypoSolver.sanitizeInputData(input, pattern);
            int totalSteps = message.getInput().length() * message.getPattern().length();
            statusReporter.setTotalSteps(totalSteps);
            PatternTypoSolver.verifyInputData(input, pattern);
            PatternTypoSolver.solveTask(statusReporter, message.getInput(), message.getPattern());
        } catch (InvalidInputDataException | InterruptedException e ) {
            statusReporter.failed(e.getMessage());
        }
    }
}