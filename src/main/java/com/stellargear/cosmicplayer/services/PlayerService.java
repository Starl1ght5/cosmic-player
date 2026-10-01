package com.stellargear.cosmicplayer.services;

import com.stellargear.cosmicplayer.models.PlayerState;
import com.stellargear.cosmicplayer.models.Song;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

public class PlayerService {

    private final MediaPlayerFactory factory = new MediaPlayerFactory("--no-video");
    private final MediaPlayer mediaPlayer;

    private final ReadOnlyObjectWrapper<PlayerState> state = new ReadOnlyObjectWrapper<>(PlayerState.STOPPED);
    private final ReadOnlyObjectWrapper<Song> currentSong = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyLongWrapper time = new ReadOnlyLongWrapper();
    private final ReadOnlyLongWrapper length = new ReadOnlyLongWrapper();
    private final DoubleProperty volume = new SimpleDoubleProperty(1.0);

    private Runnable onEndReached;

    public PlayerService() {
        mediaPlayer = factory.mediaPlayers().newMediaPlayer();

        volume.addListener((obs, old, value) -> applyVolume(value.doubleValue()));

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mp) {
                Platform.runLater(() -> {
                    applyVolume(volume.get());
                    state.set(PlayerState.PLAYING);
                });
            }

            @Override
            public void paused(MediaPlayer mp) {
                Platform.runLater(() -> state.set(PlayerState.PAUSED));
            }

            @Override
            public void stopped(MediaPlayer mp) {
                Platform.runLater(() -> {
                    state.set(PlayerState.STOPPED);
                    time.set(0);
                });
            }

            @Override
            public void timeChanged(MediaPlayer mp, long newTime) {
                Platform.runLater(() -> time.set(newTime));
            }

            @Override
            public void lengthChanged(MediaPlayer mp, long newLength) {
                Platform.runLater(() -> length.set(newLength));
            }

            @Override
            public void finished(MediaPlayer mp) {
                Platform.runLater(() -> {
                    state.set(PlayerState.STOPPED);
                    time.set(0);
                    if (onEndReached != null) {
                        onEndReached.run();
                    }
                });
            }
        });
    }

    // ---------- Comandos ----------

    public void playOrResume(Song song) {
        boolean sameSong = song.equals(currentSong.get());

        if (!sameSong) {
            start(song);
            return;
        }

        switch (mediaPlayer.status().state()) {
            case PLAYING -> mediaPlayer.controls().pause();
            case PAUSED -> mediaPlayer.controls().play();
            default -> start(song);
        }
    }

    public void seek(long timeMs) {
        mediaPlayer.controls().setTime(timeMs);
        time.set(timeMs);
    }

    public void setOnEndReached(Runnable callback) {
        this.onEndReached = callback;
    }

    public void release() {
        mediaPlayer.release();
        factory.release();
    }

    // ---------- Properties ----------

    public ReadOnlyObjectProperty<PlayerState> stateProperty() {
        return state.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Song> currentSongProperty() {
        return currentSong.getReadOnlyProperty();
    }

    public ReadOnlyLongProperty timeProperty() {
        return time.getReadOnlyProperty();
    }

    public ReadOnlyLongProperty lengthProperty() {
        return length.getReadOnlyProperty();
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    // ---------- Internos ----------

    private void start(Song song) {
        currentSong.set(song);
        time.set(0);
        length.set(song.durationMs());
        mediaPlayer.media().play(song.file().getAbsolutePath());
    }

    private void applyVolume(double value) {
        value = Math.clamp(value, 0.0, 1.0);
        if (value <= 0.0) {
            mediaPlayer.audio().setVolume(0);
            return;
        }
        double db = -11.0 * (1 - value);
        double gain = Math.pow(10, db / 20.0);
        mediaPlayer.audio().setVolume((int) Math.round(gain * 100));
    }
}