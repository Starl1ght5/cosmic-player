package com.stellargear.cosmicplayer.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Immutable representation of a user playlist.
 *
 * <p>Since records are immutable, the {@code with*} and {@code without*}
 * methods return new {@code Playlist} instances instead of modifying
 * the current one.</p>
 *
 * <p>The {@code files} list is defensively copied, so changes to the
 * original list passed to the constructor do not affect the playlist.</p>
 *
 * @param id    unique identifier of the playlist
 * @param name  display name of the playlist
 * @param files tracks contained in the playlist (never {@code null})
 */
public record Playlist(String id, String name, List<File> files) {

    public Playlist {
        files = List.copyOf(files);
    }

    /**
     * Creates an empty playlist with a new unique ID.
     *
     * @param name the display name for the new playlist
     * @return a new empty playlist
     */
    public static Playlist create(String name) {
        return new Playlist(UUID.randomUUID().toString(), name, List.of());
    }

    /**
     * Returns a copy of this playlist with the given name.
     *
     * @param newName the new display name
     * @return a new playlist instance
     */
    public Playlist withName(String newName) {
        return new Playlist(id, newName, files);
    }

    /**
     * Returns a copy of this playlist with the given file added.
     * If the file is already present, this instance is returned unchanged.
     *
     * @param file the file to add
     * @return a new playlist instance, or {@code this} if the file was already present
     */
    public Playlist withFile(File file) {
        if (files.contains(file)) return this;
        List<File> copy = new ArrayList<>(files);
        copy.add(file);
        return new Playlist(id, name, copy);
    }

    /**
     * Returns a copy of this playlist with the given file removed.
     *
     * @param file the file to remove
     * @return a new playlist instance
     */
    public Playlist withoutFile(File file) {
        List<File> copy = new ArrayList<>(files);
        copy.remove(file);
        return new Playlist(id, name, copy);
    }
}