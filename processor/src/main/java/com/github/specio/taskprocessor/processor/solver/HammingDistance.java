package com.github.specio.taskprocessor.processor.solver;


import com.github.specio.taskprocessor.processor.model.StatusReporter;
import org.apache.commons.lang3.NotImplementedException;

class HammingDistance {
    private final String pattern;
    private final String input;
    private final StatusReporter statusReporter;

    /**
     * Creates hamming distance instance
     *
     * @param statusReporter Status instance to report progress
     * @param pattern        First string to compare
     * @param input          Second string to compare (at least of patter size)
     */
    public HammingDistance(StatusReporter statusReporter, String pattern, String input) {
        throw new NotImplementedException();
    }


    /**
     * Returns hamming distance between PATTERN and equal-sized substring in INPUT at given OFFSET (window)
     * <p>
     * If reaches LIMIT, stops computation and returns current distance.
     *
     * @param offset an offset of a substring within INPUT string
     * @param limit  the limit, when reached computation is stopped
     * @return hamming distance, trimmed do limit if reached first
     */
    public int calculateUntilReachesLimit(int offset, int limit) throws InterruptedException {
        throw new NotImplementedException();
    }
}