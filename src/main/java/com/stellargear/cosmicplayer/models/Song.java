package com.stellargear.cosmicplayer.models;

import java.io.File;

public record Song(File file,
                   String title,
                   String artist, String album,
                   long durationMs,
                   byte[] coverArt) {

    @Override
    public boolean equals(Object o) {
        return o instanceof Song other && file.equals(other.file);
    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }
}

