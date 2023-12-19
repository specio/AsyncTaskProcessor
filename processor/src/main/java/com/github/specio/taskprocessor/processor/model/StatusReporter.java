package com.github.specio.taskprocessor.processor.model;

import com.github.specio.taskprocessor.processor.dto.TaskUpdateDto;
import com.github.specio.taskprocessor.processor.mapper.StatusMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Getter
public class StatusReporter {
    private final KafkaTemplate<String, TaskUpdateDto> kafkaTemplate;
    private final StatusMapper mapper;
    private final UUID taskId;
    @Setter
    private int totalSteps;

    private String timestamp;
    private int progress;
    private final Integer offset = null;
    private final Integer typos = null;

    public void complete(int offset, int typos) {
        throw new NotImplementedException();
    }

    public void failed(String message) {
        throw new NotImplementedException();
    }

    public void setCurrentProgress(int progressStep) {
        setPercentProgress(progressStep * 100 / totalSteps);
    }

    private void setPercentProgress(int progress) {
        throw new NotImplementedException();
    }

}
