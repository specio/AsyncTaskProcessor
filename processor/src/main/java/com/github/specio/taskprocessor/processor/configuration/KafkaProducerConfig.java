package com.github.specio.taskprocessor.processor.configuration;

import com.github.specio.taskprocessor.processor.dto.TaskProgressDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.task-queue-partitions}")
    private int queuePartitions;

    @Bean
    public ProducerFactory<String, TaskProgressDto> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        // Disable automatic topic creation
        configProps.put("partitioner.partitionCount", "10");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public NewTopic taskqueue() {
        return new NewTopic("taskqueue", queuePartitions, (short) 1);
    }

    @Bean
    public KafkaTemplate<String, TaskProgressDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}