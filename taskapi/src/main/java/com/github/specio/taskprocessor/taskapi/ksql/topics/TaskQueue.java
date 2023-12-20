package com.github.specio.taskprocessor.taskapi.ksql.topics;

import com.github.specio.taskprocessor.taskapi.ksql.KsqlConnector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Getter
@RequiredArgsConstructor
public class TaskQueue extends KsqlTopic {
    private final String topicName = "taskqueue";
    private final String name = "TASKQUEUE";
    private final int partitions;
    private final String selectQuery = "SELECT * FROM " + name + " EMIT CHANGES;";

    String getQuery() {
        return "  CREATE STREAM " + topicName + " (id VARCHAR KEY, task_id VARCHAR, timestamp VARCHAR, input VARCHAR, pattern VARCHAR)"
                + "  WITH (KAFKA_TOPIC = '" + topicName + "',"
                + "        VALUE_FORMAT = 'JSON',"
                + "        TIMESTAMP = 'timestamp',"
                + "        TIMESTAMP_FORMAT = 'yyyy-MM-dd HH:mm:ss',"
                + "        PARTITIONS = " + partitions + ");";
    }

    public boolean isMissing(KsqlConnector ksqlConnector) {
        return ksqlConnector.listStreams().stream().noneMatch(streamInfo -> StringUtils.equals(streamInfo.getName(), name));
    }

    public void createIfMissing(KsqlConnector ksqlConnector) {
        if (isMissing(ksqlConnector)) {
            ksqlConnector.executeQuery(getQuery());
            log.info("Topic {} not found, recreating...", name);
        }
    }
}
