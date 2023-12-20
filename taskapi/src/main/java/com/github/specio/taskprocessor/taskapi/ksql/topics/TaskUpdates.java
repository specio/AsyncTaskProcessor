package com.github.specio.taskprocessor.taskapi.ksql.topics;

import com.github.specio.taskprocessor.taskapi.ksql.KsqlConnector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
@Getter
@RequiredArgsConstructor
public class TaskUpdates extends KsqlTopic {
    private final String topicName = "updates";
    private final String name = "UPDATES";
    private final int partitions;
    private final String selectQuery = "SELECT * FROM " + name + " EMIT CHANGES;";

    String getQuery() {
        return "  CREATE STREAM " + topicName + " (task_id VARCHAR KEY, timestamp VARCHAR, progress INT, offset INT, typos INT)"
                + "  WITH (KAFKA_TOPIC = '" + topicName + "',"
                + "        VALUE_FORMAT = 'JSON',"
                + "        TIMESTAMP = 'timestamp',"
                + "        TIMESTAMP_FORMAT = 'yyyy-MM-dd HH:mm:ss',"
                + "        PARTITIONS = " + partitions + ");";
    }

    boolean isMissing(KsqlConnector ksqlConnector) {
        return ksqlConnector.listStreams().stream().noneMatch(info -> StringUtils.equals(info.getName(), name));
    }

    public void createIfMissing(KsqlConnector ksqlConnector) {
        if (isMissing(ksqlConnector)) {
            ksqlConnector.executeQuery(getQuery());
            log.info("Topic {} not found, recreating...", name);
        }
    }
}
