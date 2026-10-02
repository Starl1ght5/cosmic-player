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

public class PlaylistStorage {

    private record Data(String id, String name, List<String> files) {}

    private final Path file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public PlaylistStorage() {
        this(Path.of(System.getProperty("user.home"), ".cosmicplayer", "playlists.json"));
    }

    public PlaylistStorage(Path file) {
        this.file = file;
    }

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