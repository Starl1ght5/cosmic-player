package com.stellargear.cosmicplayer.viewmodels;

import java.util.List;

import com.stellargear.cosmicplayer.models.PlayerState;
import com.stellargear.cosmicplayer.models.Song;
import com.stellargear.cosmicplayer.services.*;
import com.stellargear.cosmicplayer.utils.TimeFormatter;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class PlaybackViewModel {

    private final PlayerService player;
    private final PlaybackQueue queue;
    private final LibraryService library;

    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final ObjectProperty<Song> selectedSong = new SimpleObjectProperty<>();

    private final BooleanProperty shuffle = new SimpleBooleanProperty(false);

    private final StringBinding title;
    private final StringBinding artist;
    private final StringBinding currentTimeText;
    private final StringBinding totalTimeText;
    private final DoubleBinding progress;
    private final BooleanBinding playing;

    private static final String LIBRARY_ID = "library";

    private List<Song> queueContext = List.of();


    public PlaybackViewModel(PlayerService player, PlaybackQueue queue, LibraryService library) {
        this.player = player;
        this.queue = queue;
        this.library = library;

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

    public void playFrom(List<Song> context, Song song) {
        if (song == null) return;
        setContext(context);
        start(song);
    }

    public void togglePlay() {
        Song current = player.currentSongProperty().get();

        if (current != null) {
            player.playOrResume(current);
            return;
        }

        List<Song> all = library.getSongs();
        if (all.isEmpty()) return;
        setContext(all);
        start(queue.next());
    }

    public void next()     { start(queue.next()); }
    public void previous() { start(queue.previous()); }

    public void seek(double fraction) {
        long total = player.lengthProperty().get();
        if (total > 0) {
            player.seek((long) (Math.clamp(fraction, 0.0, 1.0) * total));
        }
    }

    public void dispose() {
        player.release();
    }

    private void setContext(List<Song> context) {
        if (!queueContext.equals(context)) {
            queueContext = List.copyOf(context);
            queue.setSongs(queueContext);
        }
    }

    private void start(Song song) {
        if (song == null) return;
        queue.select(song);
        player.playOrResume(song);
    }

    // ---------- Estado que la UI observa ----------

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