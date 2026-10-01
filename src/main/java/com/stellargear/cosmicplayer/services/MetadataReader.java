package com.stellargear.cosmicplayer.services;

import com.stellargear.cosmicplayer.models.Song;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MetadataReader {

    private static final String UNKNOWN_ARTIST = "Unknown Artist";
    private static final String UNKNOWN_ALBUM = "Unknown Album";

    static {
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
    }

    public Song readMetadata(File file) {
        try {
            AudioFile audio = AudioFileIO.read(file);
            long durationMs = audio.getAudioHeader().getTrackLength() * 1000L;
            Tag tag = audio.getTag();

            if (tag == null) {
                return fallback(file, durationMs);
            }

            return new Song(
                    file,
                    orDefault(tag.getFirst(FieldKey.TITLE), nameWithoutExtension(file)),
                    orDefault(tag.getFirst(FieldKey.ARTIST), UNKNOWN_ARTIST),
                    orDefault(tag.getFirst(FieldKey.ALBUM), UNKNOWN_ALBUM),
                    durationMs,
                    readCover(tag)
            );
        } catch (Exception e) {
            return fallback(file, 0L);
        }
    }

    private byte[] readCover(Tag tag) {
        Artwork artwork = tag.getFirstArtwork();
        return artwork != null ? artwork.getBinaryData() : null;
    }

    private Song fallback(File file, long durationMs) {
        return new Song(file, nameWithoutExtension(file), UNKNOWN_ARTIST, UNKNOWN_ALBUM, durationMs, null);
    }

    private static String orDefault(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }

    private static String nameWithoutExtension(File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        return dot > 0 ? name.substring(0, dot) : name;
    }
}
