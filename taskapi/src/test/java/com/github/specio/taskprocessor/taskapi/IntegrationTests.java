package com.github.specio.taskprocessor.taskapi;

import com.github.specio.taskprocessor.taskapi.dto.TaskDto;
import com.github.specio.taskprocessor.taskapi.dto.TaskParamsDto;
import com.github.specio.taskprocessor.taskapi.utils.TaskApiUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@Testcontainers
class IntegrationTests {

    static DockerImageName KAFKA_TEST_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:6.2.1");
    static DockerImageName KSQL_TEST_IMAGE = DockerImageName.parse("confluentinc/ksqldb-server:0.19.0");
    public static KafkaContainer kafkaContainer;
    public static GenericContainer<?> ksqldbContainer;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("ksqldb.host", ksqldbContainer::getHost);
        registry.add("ksqldb.port", ksqldbContainer::getFirstMappedPort);
    }

    @LocalServerPort
    private int port;

    private final TaskApiUtils utils = new TaskApiUtils();

    @BeforeEach
    void init() {
        utils.setPort(port);
    }

    @BeforeAll
    public static void setUp() {
        Network network = Network.newNetwork();
        kafkaContainer = new KafkaContainer(KAFKA_TEST_IMAGE)
                .withNetwork(network);
        kafkaContainer.start();

        String kafka = kafkaContainer.getContainerId().substring(0, 12) + ":9092";

        ksqldbContainer = new GenericContainer<>(KSQL_TEST_IMAGE)
                .withNetwork(network)
                .withExposedPorts(8088)
                .dependsOn(kafkaContainer)
                .withEnv("KSQL_LISTENERS", "http://0.0.0.0:8088")
                .withEnv("KSQL_BOOTSTRAP_SERVERS", kafka)
                .withEnv("KSQL_KSQL_LOGGING_PROCESSING_STREAM_AUTO_CREATE", "true")
                .withEnv("KSQL_KSQL_LOGGING_PROCESSING_TOPIC_AUTO_CREATE", "true");
        ksqldbContainer.start();
    }

    @Test
    void createTaskShouldReturnUUID() {
        var receivedId = utils.postTask(new TaskParamsDto("CDQ", "ZAAABCDQ"));
        assertInstanceOf(UUID.class, receivedId);
    }

    @Test
    void createTaskShouldIncrementTotalTaskCount() throws InterruptedException {
        int initialAmount = utils.getAllTasks().size();
        utils.postTask(new TaskParamsDto("CDQ", "ABCDQ"));
        Thread.sleep(1000); //Wait for async task to propagate
        assertEquals(++initialAmount, utils.getAllTasks().size());
        utils.postTask(new TaskParamsDto("CDQ", "ABCDQ"));
        Thread.sleep(500); //Wait for async task to propagate
        assertEquals(++initialAmount, utils.getAllTasks().size());
        utils.postTask(new TaskParamsDto("CDQ", "ABCDQ"));
        Thread.sleep(500); //Wait for async task to propagate
        assertEquals(++initialAmount, utils.getAllTasks().size());
    }

    @Test
    void postAndGetTaskShouldReturnValidTaskDetails() throws InterruptedException {

        var id = utils.postTask(new TaskParamsDto("ABC", "ABCDQ"));
        Thread.sleep(500); //Wait for async task to propagate
        TaskDto taskDetails = utils.getTask(id);
        assertEquals(id, taskDetails.id());
    }
}
