package com.stellargear.cosmicplayer.services;

import java.io.File;
import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.stellargear.cosmicplayer.services.PlayerService.Song;

public class FileService {

    public List<File> getSongFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles(File::isFile);
        if (files == null) return List.of();
        return Arrays.stream(files)
            .sorted(Comparator.comparing(File::getName))
            .toList();
    }

    public List<Song> getSongs(String folderPath) {
        return getSongFiles(folderPath).stream()
            .map(f -> {
                SongMetadata meta = readMetadata(f);
                return new Song(f, meta.title(), meta.artist(), meta.length());
            })
            .sorted(Comparator.comparing(
                (Song s) -> s.title() == null ? "" : s.title(),
                String.CASE_INSENSITIVE_ORDER
            ))
            .toList();
    }

    public SongMetadata readMetadata(File file) {
        try {
            AudioFile f = AudioFileIO.read(file);
            Tag tag = f.getTag();

            String title = tag.getFirst(FieldKey.TITLE);
            String artist = tag.getFirst(FieldKey.ARTIST);
            int dur = f.getAudioHeader().getTrackLength();
            String length = String.format("%02d:%02d", dur / 60, dur % 60);

            return new SongMetadata(title, artist, length);
        } catch (Exception e) {
            return new SongMetadata(file.getName(), file.getName(), file.getName());
        }
    }

    public record SongMetadata(String title, String artist, String length) {}
}
