package com.stellargear.cosmicplayer.services;

import java.io.File;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class PlayerService {

    private final MediaPlayerFactory factory = new MediaPlayerFactory();
    private final MediaPlayer mediaPlayer;

    public PlayerService() {
        mediaPlayer = factory.mediaPlayers().newMediaPlayer();
    }

    public void playSong(File song, double val) {
        changeVolume(val);
        mediaPlayer.media().play(song.getAbsolutePath());
    }

    public void pause() {
        mediaPlayer.controls().pause();
    }

    public void stop() {
        mediaPlayer.controls().stop();
    }

    public void changeVolume(double value) {
        value = Math.max(0.0, Math.min(1.0, value));

        if (value <= 0.0) {
            mediaPlayer.audio().setVolume(0);
            return;
        }

        double db = -12.0 * (1 - value);
        double gain = Math.pow(10, db / 20.0);
        int vol = (int) Math.round(gain * 100);

        mediaPlayer.audio().setVolume(vol);
    }

    public boolean isPlaying() {
        return mediaPlayer.status().isPlaying();
    }

    public void release() {
        mediaPlayer.release();
        factory.release();
    }
}
