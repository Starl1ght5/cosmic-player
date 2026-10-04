package com.stellargear.cosmicplayer.services;

import com.stellargear.cosmicplayer.models.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryService {

    private static final Set<String> AUDIO_EXTENSIONS = Set.of("mp3", "flac", "wav", "ogg", "m4a", "opus");

    private final MetadataReader metadataReader;
    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final ObservableList<Song> readOnlySongs = FXCollections.unmodifiableObservableList(songs);
    private Map<File, Song> index = Map.of();

    public LibraryService(MetadataReader metadataReader) {
        this.metadataReader = metadataReader;
    }

    public ObservableList<Song> getSongs() {
        return readOnlySongs;
    }

    public Optional<Song> find(File file) {
        return Optional.ofNullable(index.get(file));
    }

    public List<Song> scan(String folderPath) {
        File[] files = new File(folderPath).listFiles(File::isFile);
        if (files == null) return List.of();

        return Arrays.stream(files)
                .filter(this::isAudioFile)
                .map(metadataReader::readMetadata)
                .sorted(Comparator.comparing(Song::title, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public void replaceAll(List<Song> loaded) {
        index = loaded.stream().collect(Collectors.toMap(Song::file, s -> s, (a, b) -> a));
        songs.setAll(loaded);
    }

    private boolean isAudioFile(File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) return false;
        String ext = name.substring(dot + 1).toLowerCase(Locale.ROOT);
        return AUDIO_EXTENSIONS.contains(ext);
    }

}
