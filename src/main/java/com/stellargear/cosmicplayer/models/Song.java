package com.stellargear.cosmicplayer.models;

import java.io.File;

/**
 * Immutable representation of a single song in the library.
 *
 * <p>Equality between songs is based only on {@link #file()}: two songs are
 * considered the same if they point to the same file, regardless of their
 * metadata (title, artist, etc.).</p>
 *
 * @param file       the audio file on disk
 * @param title      the song title
 * @param artist     the artist name
 * @param album      the album name
 * @param durationMs the duration of the song in milliseconds
 * @param coverArt   the embedded cover artwork, or {@code null} if the file has none
 */
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