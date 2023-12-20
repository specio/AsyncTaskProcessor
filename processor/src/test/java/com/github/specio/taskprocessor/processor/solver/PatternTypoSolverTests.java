package com.github.specio.taskprocessor.processor.solver;

import com.github.specio.taskprocessor.processor.model.ProgressTracker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;


@SpringBootTest
public class PatternTypoSolverTests {

    @MockBean
    private ProgressTracker progressTracker;

    @ParameterizedTest
    @MethodSource
    public void testSolverInputPositive(TaskParams params, int expectedPosition, int excpectedTypos) throws InterruptedException {
        PatternTypoSolver.solveTask(progressTracker, params.input(), params.pattern());
        verify(progressTracker).complete(expectedPosition, excpectedTypos);
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
}
