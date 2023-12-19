package com.github.specio.taskprocessor.taskapi.ksql;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class KsqlClientConfiguration {
    public static final String CREATE_UPDATES_STREAM = "  CREATE STREAM updates (task_id VARCHAR KEY, timestamp VARCHAR, progress INT, offset INT, typos INT)"
            + "  WITH (KAFKA_TOPIC = 'updates',"
            + "        VALUE_FORMAT = 'JSON',"
            + "        TIMESTAMP = 'timestamp',"
            + "        TIMESTAMP_FORMAT = 'yyyy-MM-dd HH:mm:ss',"
            + "        PARTITIONS = 10);";

    public static final String CREATE_TASK_QUEUE_STREAM = "  CREATE STREAM taskqueue (id VARCHAR KEY, task_id VARCHAR, timestamp VARCHAR, input VARCHAR, pattern VARCHAR)"
            + "  WITH (KAFKA_TOPIC = 'taskqueue',"
            + "        VALUE_FORMAT = 'JSON',"
            + "        TIMESTAMP = 'timestamp',"
            + "        TIMESTAMP_FORMAT = 'yyyy-MM-dd HH:mm:ss',"
            + "        PARTITIONS = 10);";

    public static final String CREATE_TASKS_TABLE = "  CREATE TABLE tasks AS"
            + "    SELECT"
            + "      task_id,"
            + "      LATEST_BY_OFFSET(timestamp) AS timestamp, "
            + "      LATEST_BY_OFFSET(progress) AS progress, "
            + "      LATEST_BY_OFFSET(offset) AS offset, "
            + "      LATEST_BY_OFFSET(typos) AS typos "
            + "  FROM updates"
            + "  GROUP BY task_id"
            + "  EMIT CHANGES;";

    private static final String TASKS_QUERY = "SELECT * FROM tasks EMIT CHANGES;";

    static final String UPDATES_STREAM = "updates";

    static final String TASK_QUEUE_STREAM = "TASKQUEUE";


    @Bean
    Client ksqlClient(@Value("${ksqldb.host}") String host,
                      @Value("${ksqldb.port}") int port) {
        ClientOptions options = ClientOptions.create()
                .setHost(host)
                .setPort(port);
        return Client.create(options);
    }

}