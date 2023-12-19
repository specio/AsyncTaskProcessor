package com.github.specio.taskprocessor.taskapi.ksql;

import io.confluent.ksql.api.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class KsqlConnector {
    @Autowired
    private final Client ksqlClient;

    private static final Map<String, Object> PROPERTIES = Map.of(
            "auto.offset.reset", "earliest",
            "ksql.query.pull.table.scan.enabled", true
    );

    public Optional<Row> getTask(UUID id) throws ExecutionException, InterruptedException, TimeoutException {
        String pullQuery = "SELECT * FROM tasks WHERE task_id = '" + id.toString() + "';";
        return ksqlClient.executeQuery(pullQuery, PROPERTIES)
                .get(10, TimeUnit.SECONDS).stream().findFirst();
    }

    public List<Row> getTaskIds() {
        String pullQuery = "SELECT TASK_ID FROM tasks;";
        try {
            return ksqlClient.executeQuery(pullQuery, PROPERTIES)
                    .get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            log.info("Failed to retrieve - TIMEOUT");
            throw new RuntimeException(e);
        }
    }

    public List<Row> getTasks() {
        String pullQuery = "SELECT * FROM tasks;";
        try {
            return ksqlClient.executeQuery(pullQuery, PROPERTIES)
                    .get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            log.info("Failed to retrieve - TIMEOUT");
            throw new RuntimeException(e);
        }
    }


    public CompletableFuture<Void> insert(TaskTopic stream, KsqlObject row) {
        return ksqlClient.insertInto(stream.getStringValue(), row);
    }

    public CompletableFuture<Void> insert(TaskTopic stream, Collection<KsqlObject> rows) {
        return CompletableFuture.allOf(
                rows.stream()
                        .map(row -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return ksqlClient.insertInto(stream.getStringValue(), row);
                        })
                        .toArray(CompletableFuture[]::new)
        );
    }

    ExecuteStatementResult executeQuery(String query) {
        try {
            ExecuteStatementResult result = ksqlClient.executeStatement(query, PROPERTIES).get();
            log.info("Result: {}", result.queryId().orElse(null));
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error: ", e);
            throw new RuntimeException(e);
        }
    }

    public List<StreamInfo> listStreams() {
        try {
            return ksqlClient.listStreams().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error: ", e);
            throw new RuntimeException(e);
        }
    }

    public List<TableInfo> listTables() {
        try {
            return ksqlClient.listTables().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error: ", e);
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<StreamedQueryResult> streamQuery(TaskTopic taskTopic) {
        String pushQuery = "SELECT * FROM " + TaskTopic.QUEUE + " EMIT CHANGES;";
        return ksqlClient.streamQuery(pushQuery, PROPERTIES);
    }
}
