package com.stellargear.cosmicplayer.services;

import com.stellargear.cosmicplayer.models.PlayerState;
import com.stellargear.cosmicplayer.models.Song;

import com.stellargear.cosmicplayer.services.persistence.VLCBundler;
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

/**
 * Service responsible for audio playback through VLC.
 *
 * <p>Exposes observable JavaFX properties ({@link #stateProperty()},
 * {@link #currentSongProperty()}, {@link #timeProperty()}, {@link #lengthProperty()},
 * {@link #volumeProperty()}) so UI components can bind to the playback state.</p>
 *
 * <p>All media player events are marshaled onto the JavaFX application thread
 * before updating the observable properties.</p>
 *
 * @author Starl1ght5
 * @since 1.0
 */
public class PlayerService {

    private final MediaPlayerFactory factory = new MediaPlayerFactory(VLCBundler.discovery(), "--no-video");
    private final MediaPlayer mediaPlayer;

    private final ReadOnlyObjectWrapper<PlayerState> state = new ReadOnlyObjectWrapper<>(PlayerState.STOPPED);
    private final ReadOnlyObjectWrapper<Song> currentSong = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyLongWrapper time = new ReadOnlyLongWrapper();
    private final ReadOnlyLongWrapper length = new ReadOnlyLongWrapper();
    private final DoubleProperty volume = new SimpleDoubleProperty(1.0);

    private Runnable onEndReached;

    /**
     * Creates a new player service and wires up the media player event listeners.
     */
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

    // ---------- Commands ----------

    /**
     * Plays the given song, resumes the current one, or toggles pause,
     * depending on the current player state:
     * <ul>
     *   <li>If {@code song} differs from the current one, playback starts from the beginning.</li>
     *   <li>If it is the same song and playback is running, it pauses.</li>
     *   <li>If it is the same song and playback is paused, it resumes.</li>
     * </ul>
     *
     * @param song the song to play
     */
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

    /**
     * Seeks to the given position in the current track.
     *
     * @param timeMs target position in milliseconds
     */
    public void seek(long timeMs) {
        mediaPlayer.controls().setTime(timeMs);
        time.set(timeMs);
    }

    /**
     * Registers a callback invoked when the current track finishes playing.
     *
     * @param callback the callback to run, or {@code null} to clear it
     */
    public void setOnEndReached(Runnable callback) {
        this.onEndReached = callback;
    }

    /**
     * Releases the media player and the VLC factory. Call this when the
     * service is no longer needed to free native resources.
     */
    public void release() {
        mediaPlayer.release();
        factory.release();
    }

    // ---------- Properties ----------

    /** @return the current playback state, for UI binding */
    public ReadOnlyObjectProperty<PlayerState> stateProperty() {
        return state.getReadOnlyProperty();
    }

    /** @return the currently loaded song, for UI binding */
    public ReadOnlyObjectProperty<Song> currentSongProperty() {
        return currentSong.getReadOnlyProperty();
    }

    /** @return the current playback position in milliseconds, for UI binding */
    public ReadOnlyLongProperty timeProperty() {
        return time.getReadOnlyProperty();
    }

    /** @return the total duration of the current song in milliseconds, for UI binding */
    public ReadOnlyLongProperty lengthProperty() {
        return length.getReadOnlyProperty();
    }

    /** @return the volume (0.0 to 1.0), bindable and observable */
    public DoubleProperty volumeProperty() {
        return volume;
    }

    // ---------- Internal ----------

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
        // Converts linear volume (0.0–1.0) to VLC's logarithmic scale
        double db = -11.0 * (1 - value);
        double gain = Math.pow(10, db / 20.0);
        mediaPlayer.audio().setVolume((int) Math.round(gain * 100));
    }
}