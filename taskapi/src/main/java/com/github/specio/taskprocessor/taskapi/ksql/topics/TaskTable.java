package com.github.specio.taskprocessor.taskapi.ksql.topics;

import com.github.specio.taskprocessor.taskapi.ksql.KsqlConnector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Getter
@RequiredArgsConstructor
public class TaskTable extends KsqlTopic {
    private final String topicName = "tasks";
    private final String name = "TASKS";
    private final int partitions;
    private final String selectQuery = "SELECT * FROM " + name + " EMIT CHANGES;";

    String getQuery() {
        return "  CREATE TABLE  " + topicName + " AS"
                + "    SELECT"
                + "      task_id,"
                + "      LATEST_BY_OFFSET(timestamp) AS timestamp, "
                + "      LATEST_BY_OFFSET(progress) AS progress, "
                + "      LATEST_BY_OFFSET(offset) AS offset, "
                + "      LATEST_BY_OFFSET(typos) AS typos "
                + "  FROM updates"
                + "  GROUP BY task_id"
                + "  EMIT CHANGES;";
    }

    boolean isMissing(KsqlConnector ksqlConnector) {
        return ksqlConnector.listTables().stream().noneMatch(info -> StringUtils.equals(info.getName(), name));
    }

    public void createIfMissing(KsqlConnector ksqlConnector) {
        if (isMissing(ksqlConnector)) {
            ksqlConnector.executeQuery(getQuery());
            log.info("Topic {} not found, recreating...", name);
        }
    }
}
