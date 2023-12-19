package com.github.specio.taskprocessor.processor.model;

import com.github.specio.taskprocessor.processor.dto.TaskUpdateDto;
import com.github.specio.taskprocessor.processor.mapper.StatusMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
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
    private Integer offset = null;
    private Integer typos = null;

    public void complete(int offset, int typos) {
        this.offset = offset;
        this.typos = typos;
        setPercentProgress(100);
    }

    public void failed(String message) {
        this.offset = -1;
        this.typos = -1;
        setPercentProgress(100);
        log.error("FAILED: ({}) - {}", taskId, message);
    }

    public void setCurrentProgress(int progressStep) {
        setPercentProgress(progressStep * 100 / totalSteps);
    }

    private void setPercentProgress(int progress) {
        this.progress = progress;
        this.timestamp  =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date.from(Instant.now()));
        kafkaTemplate.send("updates", taskId.toString(), mapper.statusToTaskUpdate(this));
    }

}
