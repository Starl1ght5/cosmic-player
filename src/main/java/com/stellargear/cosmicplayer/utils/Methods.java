package com.stellargear.cosmicplayer.utils;

import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class Methods {

    private Methods () {}

    private static final int MAX_CACHE_ENTRIES = 300;
    
    private static final Map<File, Image> THUMBNAIL_CACHE = new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<File, Image> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        }
    };
    
    public static Image toImage(byte[] coverArtBytes, double size) {
        if (coverArtBytes == null || coverArtBytes.length == 0) {
            return new Image(Objects.requireNonNull(Methods.class.getResourceAsStream("/icons/IcRoundPlayArrow.png")),
                size, size, true, true);
        }
        return new Image(new ByteArrayInputStream(coverArtBytes), size, size, true, true);
    }

    public static Image toCachedThumbnail(File songFile, byte[] coverArtBytes, double size) {
        return THUMBNAIL_CACHE.computeIfAbsent(songFile, f -> toImage(coverArtBytes, size));
    }
}
