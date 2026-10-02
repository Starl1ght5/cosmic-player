package com.stellargear.cosmicplayer.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record Playlist(String id, String name, List<File> files) {

    public Playlist {
        files = List.copyOf(files);
    }

    public static Playlist create(String name) {
        return new Playlist(UUID.randomUUID().toString(), name, List.of());
    }

    public Playlist withName(String newName) {
        return new Playlist(id, newName, files);
    }

    public Playlist withFile(File file) {
        if (files.contains(file)) return this;
        List<File> copy = new ArrayList<>(files);
        copy.add(file);
        return new Playlist(id, name, copy);
    }

    public Playlist withoutFile(File file) {
        List<File> copy = new ArrayList<>(files);
        copy.remove(file);
        return new Playlist(id, name, copy);
    }
}