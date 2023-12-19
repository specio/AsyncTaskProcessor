package com.github.specio.taskprocessor.processor.service;

import com.github.specio.taskprocessor.processor.dto.StatusDto;
import com.github.specio.taskprocessor.processor.processor.TaskProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessorService {

    @Value(value = "${spring.kafka.listener.concurrency}")
    int concurrency;

    private final TaskProcessor taskProcessor;

    public StatusDto getCurrentStatus() {
        return new StatusDto(concurrency);
    }

}
