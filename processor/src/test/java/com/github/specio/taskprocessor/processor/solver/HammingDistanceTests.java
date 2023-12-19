package com.github.specio.taskprocessor.processor.solver;

import com.github.specio.taskprocessor.processor.dto.TaskUpdateDto;
import com.github.specio.taskprocessor.processor.model.StatusReporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
public class HammingDistanceTests {

    private TaskParams params;


    @MockBean
    private KafkaTemplate<String, TaskUpdateDto> kafkaTemplate;

    @MockBean
    private StatusReporter statusReporter;

    @BeforeEach
    public void init() {
        params = new TaskParams("ABC", "BABCABDEFGHIJKLMNZZZZZZAAAAAXXXXXX");
    }

    @ParameterizedTest
    @MethodSource
    public void testCalculateUntilReachesLimit(int offset, int limit, int expectedDistance) throws InterruptedException {

        HammingDistance hammingDistance = new HammingDistance(statusReporter, params.pattern(), params.input());
        assertEquals(expectedDistance, hammingDistance.calculateUntilReachesLimit(offset, limit));

        int expectedStatusUpdateCount = Math.min(limit,params.pattern().length());
        verify(statusReporter, times(expectedStatusUpdateCount)).setCurrentProgress(anyInt());
    }

    private static Stream<Arguments> testCalculateUntilReachesLimit() {
        return Stream.of(
                arguments(0, Integer.MAX_VALUE, 3),
                arguments(1, Integer.MAX_VALUE, 0),
                arguments(2, Integer.MAX_VALUE, 3),
                arguments(2, 1, 1), // 3 limited to 1
                arguments(3, 2, 2), // 3 limited to 2
                arguments(3, Integer.MAX_VALUE, 3),
                arguments(4, Integer.MAX_VALUE, 1),
                arguments(5, Integer.MAX_VALUE, 3)
        );
    }
}
