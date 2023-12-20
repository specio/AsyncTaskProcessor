package com.github.specio.taskprocessor.processor.solver;

import com.github.specio.taskprocessor.processor.model.ProgressTracker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PatternTypoSolver{
    private final ProgressTracker progressTracker;
    private final String input;
    private final String pattern;

    /**
     * Run sliding window of size equal to pattern, finding first best match
     * in input with lowest hamming distance between window and pattern
     * <p>
     * Sends current progress via external ProgressTracker
     *
     * @param progressTracker Tracks progress a result of solved task
     * @param input  Input string of size greater than pattern
     * @param pattern  Pattern found in input string with best match
     */
    public static void solveTask(ProgressTracker progressTracker, String input, String pattern) throws InterruptedException {
        PatternTypoSolver solver = new PatternTypoSolver(progressTracker,input,pattern);
        solver.solve();
    }

    /**
     * Run sliding window of size equal to pattern, finding first best match
     * in input with lowest hamming distance between window and pattern
     * <p>
     * Sends current progress via ProgressTracker to Kafka Topic
     */
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
}
