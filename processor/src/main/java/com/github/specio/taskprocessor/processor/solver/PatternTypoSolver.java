package com.github.specio.taskprocessor.processor.solver;

import com.github.specio.taskprocessor.processor.exception.InvalidInputDataException;
import com.github.specio.taskprocessor.processor.model.ProgressTracker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class PatternTypoSolver{
    private final ProgressTracker progressTracker;
    private final String input;
    private final String pattern;

    public static void solveTask(ProgressTracker progressTracker, String input, String pattern) throws InterruptedException {
        PatternTypoSolver solver = new PatternTypoSolver(progressTracker,input,pattern);
        solver.solve();
    }
    private void solve() throws InterruptedException {
        HammingDistance hammingDistance = new HammingDistance(progressTracker, pattern, input);

            int typos = Integer.MAX_VALUE;
            int position = -1;
            int maxWindowOffset = input.length() - pattern.length() + 1;
            for (int i = 0; i < maxWindowOffset; i++) {
                int distance = hammingDistance.calculateUntilReachesLimit(i, typos);
                if (distance < typos) {
                    typos = distance;
                    position = i;
                    log.info("Thread ({}) Updated position ({}): {}", Thread.currentThread().getName(), position, distance);
                    if (typos == 0) break;
                }
            }
            progressTracker.complete(position,typos);
    }

    public static void verifyInputData(String input, String pattern) throws InvalidInputDataException {
        if (StringUtils.isAnyBlank(input, pattern))
            throw new InvalidInputDataException("Passed null parameter");
        if (pattern.length() > input.length())
            throw new InvalidInputDataException("Pattern is longer than input");
    }
}
