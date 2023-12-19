package com.github.specio.taskprocessor.taskapi.ksql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KsqlSchemaCreateListener implements ApplicationListener<ContextRefreshedEvent> {
    private final KsqlConnector ksqlConnector;

    private final Map<String, Object> props = Map.of(
            "auto.offset.reset", "earliest",
            "ksql.query.pull.table.scan.enabled", "true"
    );

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (streamExists("UPDATES"))
            ksqlConnector.executeQuery(KsqlClientConfiguration.CREATE_UPDATES_STREAM);

        if (tableExists("TASKS"))
            ksqlConnector.executeQuery(KsqlClientConfiguration.CREATE_TASKS_TABLE);

        if (streamExists("TASKQUEUE"))
            ksqlConnector.executeQuery(KsqlClientConfiguration.CREATE_TASK_QUEUE_STREAM);

    }

    private boolean tableExists(String name) {
        return ksqlConnector.listTables().stream().noneMatch(tableInfo -> StringUtils.equals(tableInfo.getName(), name));
    }

    private boolean streamExists(String name) {
        return ksqlConnector.listStreams().stream().noneMatch(streamInfo -> StringUtils.equals(streamInfo.getName(), name));
    }


}