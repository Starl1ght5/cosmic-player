package com.stellargear.cosmicplayer.services.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.stellargear.cosmicplayer.models.Playlist;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

/**
 * Persists user playlists as a JSON file on disk.
 *
 * <p>By default the data is stored in {@code ~/.cosmicplayer/playlists.json}.
 * Saves are written atomically (via a temporary file) so a crash during
 * saving does not corrupt the existing file.</p>
 *
 * @author Starl1ght5
 * @since 1.0
 */
public class PlaylistStorage {

    /** JSON-serializable representation of a playlist (files as absolute paths). */
    private record Data(String id, String name, List<String> files) {}

    private final Path file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Creates a storage using the default file location
     * ({@code ~/.cosmicplayer/playlists.json}).
     */
    public PlaylistStorage() {
        this(Path.of(System.getProperty("user.home"), ".cosmicplayer", "playlists.json"));
    }

    /**
     * Creates a storage using the given file path.
     *
     * @param file the JSON file used for persistence
     */
    public PlaylistStorage(Path file) {
        this.file = file;
    }

    /**
     * Loads all playlists from disk.
     *
     * <p>Returns an empty list if the file does not exist, is empty, or cannot
     * be parsed. Parse and read errors are logged to standard error.</p>
     *
     * @return the loaded playlists, or an empty list on failure
     */
    public List<Playlist> load() {
        if (!Files.exists(file)) return List.of();

        try (Reader reader = Files.newBufferedReader(file)) {
            Data[] data = gson.fromJson(reader, Data[].class);
            if (data == null) return List.of();

            return Arrays.stream(data)
                    .map(d -> new Playlist(d.id(), d.name(), d.files().stream().map(File::new).toList()))
                    .toList();
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Saves all playlists to disk. The write is atomic: data goes to a
     * temporary file first and is then moved over the target file.
     * I/O errors are logged to standard error.
     *
     * @param playlists the playlists to save
     */
    public void save(List<Playlist> playlists) {
        Data[] data = playlists.stream()
                .map(p -> new Data(p.id(), p.name(), p.files().stream().map(File::getAbsolutePath).toList()))
                .toArray(Data[]::new);

        try {
            Files.createDirectories(file.getParent());
            Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
            Files.writeString(tmp, gson.toJson(data));
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}