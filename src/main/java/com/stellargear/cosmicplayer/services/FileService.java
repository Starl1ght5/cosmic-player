package com.stellargear.cosmicplayer.services;

import java.io.File;
import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import com.stellargear.cosmicplayer.services.PlayerService.Song;

public class FileService {

    private static final Set<String> AUDIO_EXTENSIONS = Set.of("mp3", "flac", "wav", "ogg", "m4a");

    public List<File> getSongFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles(File::isFile);
        if (files == null) return List.of();
        return Arrays.stream(files)
            .filter(this::isAudioFile)
            .sorted(Comparator.comparing(File::getName))
            .toList();
    }

    private boolean isAudioFile(File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) return false;
        String ext = name.substring(dot + 1).toLowerCase(Locale.ROOT);
        return AUDIO_EXTENSIONS.contains(ext);
    }

    public List<Song> getSongs(String folderPath) {
        return getSongFiles(folderPath).stream()
            .map(f -> {
                SongMetadata meta = readMetadata(f);
                return new Song(f, meta.title(), meta.artist(), meta.album(), meta.releaseDate(), meta.length(), meta.coverArt());
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
            String album = tag.getFirst(FieldKey.ALBUM);
            String releaseDate = tag.getFirst(FieldKey.ALBUM_YEAR);
            int dur = f.getAudioHeader().getTrackLength();
            String length = String.format("%02d:%02d", dur / 60, dur % 60);

            byte[] coverArtBytes = null;
            Artwork artwork = tag.getFirstArtwork();

            if (artwork != null) {
                coverArtBytes = artwork.getBinaryData();
            }

            return new SongMetadata(title, artist, album, releaseDate, length, coverArtBytes);
        } catch (Exception e) {
            return new SongMetadata(file.getName(), "Unknow Artist", "Unknow Album", "Unknow Date", "00:00", null);
        }
    }

    public record SongMetadata(String title, String artist, String album, String releaseDate, String length, byte[] coverArt) {}
}
