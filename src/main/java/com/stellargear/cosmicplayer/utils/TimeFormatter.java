package com.stellargear.cosmicplayer.utils;

public class TimeFormatter {

    private TimeFormatter() {}

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
