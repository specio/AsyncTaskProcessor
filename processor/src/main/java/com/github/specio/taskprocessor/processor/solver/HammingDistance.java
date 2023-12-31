package com.github.specio.taskprocessor.processor.solver;


import com.github.specio.taskprocessor.processor.model.ProgressTracker;

class HammingDistance {
    private final String pattern;
    private final String input;
    private final ProgressTracker progressTracker;

    /**
     * Creates hamming distance instance
     *
     * @param progressTracker   Status instance to report progress
     * @param pattern  First string to compare
     * @param input    Second string to compare (at least of patter size)
     */
    public HammingDistance(ProgressTracker progressTracker, String pattern, String input) {
        this.pattern = pattern;
        this.input = input;
        this.progressTracker = progressTracker;
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
        int current_distance = 0;
        for (int j = 0; j < pattern.length(); j++) {
            if (input.charAt(offset + j) != pattern.charAt(j)) current_distance++;
            progressTracker.setCurrentProgress((offset * pattern.length() + j));
            if (current_distance >= limit) break;
            Thread.sleep(100);
        }
        return current_distance;
    }
}