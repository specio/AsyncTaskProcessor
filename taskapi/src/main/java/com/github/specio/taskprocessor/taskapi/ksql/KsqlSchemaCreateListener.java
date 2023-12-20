package com.github.specio.taskprocessor.taskapi.ksql;

import com.github.specio.taskprocessor.taskapi.ksql.topics.TaskQueue;
import com.github.specio.taskprocessor.taskapi.ksql.topics.TaskTable;
import com.github.specio.taskprocessor.taskapi.ksql.topics.TaskUpdates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KsqlSchemaCreateListener implements ApplicationListener<ContextRefreshedEvent> {
    private final KsqlConnector ksqlConnector;

    @Value(value = "${spring.kafka.task-queue-partitions}")
    private int partitions;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        ksqlConnector.verifyConnection();

        List.of(
                new TaskUpdates(partitions),
                new TaskTable(partitions),
                new TaskQueue(partitions)
        ).forEach((channel) -> {
            channel.createIfMissing(ksqlConnector);
        });
    }
}