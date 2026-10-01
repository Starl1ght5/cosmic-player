package com.stellargear.cosmicplayer.services;

import com.stellargear.cosmicplayer.models.Song;

import java.io.File;
import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LibraryService {

    private static final Set<String> AUDIO_EXTENSIONS = Set.of("mp3", "flac", "wav", "ogg", "m4a");

    private final MetadataReader metadataReader;

    public LibraryService(MetadataReader metadataReader) {
        this.metadataReader = metadataReader;
    }

    public List<Song> loadSongs(String folderPath) {
        File[] files = new File(folderPath).listFiles(File::isFile);
        if (files == null) return List.of();

        return Arrays.stream(files)
                .filter(this::isAudioFile)
                .map(metadataReader::readMetadata)
                .sorted(Comparator.comparing(Song::title, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private boolean isAudioFile(File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) return false;
        String ext = name.substring(dot + 1).toLowerCase(Locale.ROOT);
        return AUDIO_EXTENSIONS.contains(ext);
    }

}
