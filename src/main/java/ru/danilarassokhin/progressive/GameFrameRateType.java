package ru.danilarassokhin.progressive;

/**
 * Frame rate type defines strategy of game scripts notification about update
 */
public enum GameFrameRateType {
    /**
     * All scripts will be notified in sequence they were subscribed
     */
    SEQUENCE,

    /**
     * Try to notify scripts using parallel streams
     * <p>Possibly can be faster if you have many scripts with "heavy" logic</p>
     */
    PARALLEL
}
