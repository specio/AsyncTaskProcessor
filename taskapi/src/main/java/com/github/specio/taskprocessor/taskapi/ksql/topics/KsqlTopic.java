package com.github.specio.taskprocessor.taskapi.ksql.topics;

import com.github.specio.taskprocessor.taskapi.ksql.KsqlConnector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class KsqlTopic {
    abstract String getQuery();

    abstract boolean isMissing(KsqlConnector ksqlConnector);

    abstract String getName();

    public void createIfMissing(KsqlConnector ksqlConnector) {
        if (isMissing(ksqlConnector)) {
            ksqlConnector.executeQuery(getQuery());
            log.info("Topic {} not found, recreating...", getName());
        }
    }
}
