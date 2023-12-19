package com.github.specio.taskprocessor.processor.controller;

import com.github.specio.taskprocessor.processor.dto.StatusDto;
import com.github.specio.taskprocessor.processor.service.ProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/status")
@RequiredArgsConstructor
public class ProcessorController {
    private final ProcessorService taskProcessingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public StatusDto getStatus() {
        return taskProcessingService.getCurrentStatus();
    }
}