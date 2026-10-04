package com.stellargear.cosmicplayer.viewmodels;

import com.stellargear.cosmicplayer.models.Song;
import com.stellargear.cosmicplayer.services.LibraryService;
import com.stellargear.cosmicplayer.services.persistence.SettingsService;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.File;
import java.util.List;

public class LibraryViewModel {

    private final LibraryService library;
    private final SettingsService settings;
    private final ReadOnlyBooleanWrapper loading = new ReadOnlyBooleanWrapper(false);

    public LibraryViewModel(LibraryService library, SettingsService settings) {
        this.library = library;
        this.settings = settings;
    }

    public ObservableList<Song> getSongs() {
        loadSavedFolder();
        return library.getSongs();
    }

    public ReadOnlyBooleanProperty loadingProperty() {
        return loading.getReadOnlyProperty();
    }

    public String getMusicFolder() {
        return settings.getMusicFolder();
    }

    public void loadFolder(String path) {
        settings.setMusicFolder(path);
        loading.set(true);

        Task<List<Song>> task = new Task<>() {
            @Override
            protected List<Song> call() {
                return library.scan(path);
            }
        };

        task.setOnSucceeded(e -> {
            library.replaceAll(task.getValue());
            loading.set(false);
        });
        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            loading.set(false);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public boolean loadSavedFolder() {
        String saved = settings.getMusicFolder();
        if (saved == null || !new File(saved).isDirectory()) return false;
        loadFolder(saved);
        return true;
    }
}