package com.stellargear.cosmicplayer.utils;

/**
 * Utility class for formatting time durations into human-readable text.
 *
 * <p>This class cannot be instantiated; all its methods are static.</p>
 *
 * @author Starl1ght5
 * @version 1.0
 * @since 1.0
 */
public class TimeFormatter {

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private TimeFormatter() {}

    /**
     * Converts a duration in milliseconds into a formatted time string.
     *
     * <p>Output examples:
     * <ul>
     *   <li>{@code 75000} → {@code "01:15"}</li>
     *   <li>{@code 3661000} → {@code "1:01:01"}</li>
     * </ul>
     *
     * @param ms the duration in milliseconds (negative values are treated as zero)
     * @return a string formatted as {@code "mm:ss"}, or {@code "h:mm:ss"}
     *         if the duration is one hour or longer
     *
     * @example
     * <pre>{@code
     * String time = TimeFormatter.format(90000);
     * System.out.println(time); // "01:30"
     * }</pre>
     */
    public static String format(long ms) {
        if (ms < 0) return "00:00";

        long totalSeconds = ms / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }
}