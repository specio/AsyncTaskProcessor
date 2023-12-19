package com.github.specio.taskprocessor.processor.solver;

import com.github.specio.taskprocessor.processor.dto.TaskUpdateDto;
import com.github.specio.taskprocessor.processor.exception.InvalidInputDataException;
import com.github.specio.taskprocessor.processor.model.StatusReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
public class PatternTypoSolverTests {

    private TaskParams params;

    @MockBean
    private KafkaTemplate<String, TaskUpdateDto> kafkaTemplate;

    @MockBean
    private StatusReporter statusReporter;

    @ParameterizedTest
    @MethodSource
    public void testSolverInputPositive(TaskParams params, int expectedPosition, int excpectedTypos) throws InterruptedException {
        PatternTypoSolver.solveTask(statusReporter, params.input(), params.pattern());
        verify(statusReporter).complete(expectedPosition, excpectedTypos);
    }

    private static Stream<Arguments> testSolverInputPositive() {
        return Stream.of(
                arguments(new TaskParams("BCD", "ABCD"), 1, 0),
                arguments(new TaskParams("BWD", "ABCD"), 1, 1),
                arguments(new TaskParams("CFG", "ABCDEFG"), 4, 1),
                arguments(new TaskParams("ABC", "ABCABC"), 0, 0),
                arguments(new TaskParams("TDD", "ABCDEFG"), 1, 2)
        );
    }


    @ParameterizedTest
    @MethodSource
    public void testSolverInputNullOrEmptyNegative(TaskParams params) {
        assertThrows(
                InvalidInputDataException.class,
                () -> PatternTypoSolver.sanitizeInputData(params.input(), params.pattern()));
    }

    private static Stream<Arguments> testSolverInputNullOrEmptyNegative() {
        return Stream.of(
                arguments(new TaskParams(null, null)),
                arguments(new TaskParams("BCD", "")),
                arguments(new TaskParams("", "AB")),
                arguments(new TaskParams("", "")),
                arguments(new TaskParams("ABCDE", null))
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testSolverVerifyInputDataPositive(TaskParams params) {
        assertDoesNotThrow(
                () -> PatternTypoSolver.verifyInputData(params.input(), params.pattern()));
    }

    private static Stream<Arguments> testSolverVerifyInputDataPositive() {
        return Stream.of(
                arguments(new TaskParams("BCD", "ABCDXS")),
                arguments(new TaskParams("A", "AB"))
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testSolverVerifyInputDataNegative(TaskParams params) {
        assertThrows(
                InvalidInputDataException.class,
                () -> PatternTypoSolver.verifyInputData(params.input(), params.pattern()));
    }

    private static Stream<Arguments> testSolverVerifyInputDataNegative() {
        return Stream.of(
                arguments(new TaskParams("BCD", "AB")),
                arguments(new TaskParams("ABCDE", "ABC"))
        );
    }
}
