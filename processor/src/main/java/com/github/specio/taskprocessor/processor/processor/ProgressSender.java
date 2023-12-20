package com.github.specio.taskprocessor.processor.processor;

import com.github.specio.taskprocessor.processor.dto.TaskProgressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgressSender {

    private final KafkaTemplate<String, TaskProgressDto> kafkaTemplate;

    public void sendProgressUpdate(TaskProgressDto message) {
        kafkaTemplate.send("updates", message.taskId().toString(), message);
    }
}
