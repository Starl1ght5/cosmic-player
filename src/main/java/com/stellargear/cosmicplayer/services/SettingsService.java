package com.stellargear.cosmicplayer.services;

import java.util.prefs.Preferences;


public class SettingsService {

    private static final String MUSIC_FOLDER_KEY = "music_folder";

    private final Preferences prefs = Preferences.userNodeForPackage(SettingsService.class);

    public String getMusicFolder() {
        return prefs.get(MUSIC_FOLDER_KEY, null);
    }

    public void setMusicFolder(String path) {
        prefs.put(MUSIC_FOLDER_KEY, path);
    }

    public boolean hasMusicFolder() {
        return getMusicFolder() != null;
    }
}
