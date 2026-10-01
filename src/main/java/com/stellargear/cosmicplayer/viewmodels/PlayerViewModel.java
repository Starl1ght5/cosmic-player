package com.stellargear.cosmicplayer.viewmodels;

import java.io.File;
import java.util.List;

import com.stellargear.cosmicplayer.models.PlayerState;
import com.stellargear.cosmicplayer.models.Song;
import com.stellargear.cosmicplayer.services.LibraryService;
import com.stellargear.cosmicplayer.services.PlaybackQueue;
import com.stellargear.cosmicplayer.services.PlayerService;
import com.stellargear.cosmicplayer.services.SettingsService;
import com.stellargear.cosmicplayer.utils.TimeFormatter;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class PlayerViewModel {

    private final PlayerService player;
    private final PlaybackQueue queue;
    private final LibraryService library;
    private final SettingsService settings;

    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final ObjectProperty<Song> selectedSong = new SimpleObjectProperty<>();
    private final BooleanProperty shuffle = new SimpleBooleanProperty(false);

    private final StringBinding title;
    private final StringBinding artist;
    private final StringBinding currentTimeText;
    private final StringBinding totalTimeText;
    private final DoubleBinding progress;
    private final BooleanBinding playing;

    public PlayerViewModel(PlayerService player, PlaybackQueue queue,
                           LibraryService library, SettingsService settings) {
        this.player = player;
        this.queue = queue;
        this.library = library;
        this.settings = settings;

        title = Bindings.createStringBinding(() -> {
            Song s = player.currentSongProperty().get();
            return s == null ? "" : s.title();
        }, player.currentSongProperty());

        artist = Bindings.createStringBinding(() -> {
            Song s = player.currentSongProperty().get();
            return s == null ? "" : s.artist();
        }, player.currentSongProperty());

        currentTimeText = Bindings.createStringBinding(
                () -> TimeFormatter.format(player.timeProperty().get()),
                player.timeProperty());

        totalTimeText = Bindings.createStringBinding(
                () -> TimeFormatter.format(player.lengthProperty().get()),
                player.lengthProperty());

        // 0.0 a 1.0
        progress = Bindings.createDoubleBinding(() -> {
            long total = player.lengthProperty().get();
            return total > 0 ? Math.min(1.0, player.timeProperty().get() / (double) total) : 0.0;
        }, player.timeProperty(), player.lengthProperty());

        playing = player.stateProperty().isEqualTo(PlayerState.PLAYING);

        shuffle.addListener((obs, old, value) -> queue.setShuffle(value));
        player.setOnEndReached(this::next);
    }

    // ---------- Comandos (lo que la UI puede pedir) ----------

    public void play(Song song) {
        if (song == null) return;
        queue.select(song);
        player.playOrResume(song);
    }

    public void togglePlay() {
        Song target = selectedSong.get() != null
                ? selectedSong.get()
                : player.currentSongProperty().get();

        play(target != null ? target : queue.next());
    }

    public void next() {
        play(queue.next());
    }

    public void previous() {
        play(queue.previous());
    }

    public void seek(double fraction) {
        long total = player.lengthProperty().get();
        if (total > 0) {
            player.seek((long) (Math.clamp(fraction, 0.0, 1.0) * total));
        }
    }

    public void loadFolder(String path) {
        settings.setMusicFolder(path);

        Task<List<Song>> task = new Task<>() {
            @Override
            protected List<Song> call() {
                return library.loadSongs(path);
            }
        };

        task.setOnSucceeded(e -> {
            List<Song> loaded = task.getValue();
            songs.setAll(loaded);
            queue.setSongs(loaded);
        });
        task.setOnFailed(e -> task.getException().printStackTrace());

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

    public String getMusicFolder() {
        return settings.getMusicFolder();
    }

    public void dispose() {
        player.release();
    }

    // ---------- Estado que la UI observa ----------

    public ObservableList<Song> getSongs() {
        return FXCollections.unmodifiableObservableList(songs);
    }

    public ObjectProperty<Song> selectedSongProperty() { return selectedSong; }

    public ReadOnlyObjectProperty<Song> currentSongProperty() { return player.currentSongProperty(); }

    public BooleanProperty shuffleProperty() { return shuffle; }

    public DoubleProperty volumeProperty() { return player.volumeProperty(); }

    public StringBinding titleProperty() { return title; }
    public StringBinding artistProperty() { return artist; }
    public StringBinding currentTimeTextProperty() { return currentTimeText; }
    public StringBinding totalTimeTextProperty() { return totalTimeText; }
    public DoubleBinding progressProperty() { return progress; }
    public BooleanBinding playingProperty() { return playing; }
}