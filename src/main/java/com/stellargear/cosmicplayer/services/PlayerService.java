package com.stellargear.cosmicplayer.services;

import java.io.File;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.State;

public class PlayerService {

    private final MediaPlayerFactory factory = new MediaPlayerFactory();
    private final MediaPlayer mediaPlayer;

    private double currentVolume = 1.0;
    private File currentSong;

    public PlayerService() {
        mediaPlayer = factory.mediaPlayers().newMediaPlayer();

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                applyVolume(currentVolume);
            }
        });
    }

    public void playSong(File song, double val) {
        mediaPlayer.media().play(song.getAbsolutePath());
        changeVolume(val);
        currentSong = song;
    }

    public void playOrResume(File song, double val) {
        State state = mediaPlayer.status().state();

        boolean isSameSong = currentSong != null && currentSong.equals(song);

        if (isSameSong) {
            switch (state) {
                case PLAYING:
                    mediaPlayer.controls().pause();
                    break;
                case PAUSED:
                    changeVolume(val);
                    mediaPlayer.controls().play();
                    break;
                default:
                    playSong(song, val);
                    break;
            }
        } else {
            playSong(song, val);
        }
    }

    public void changeVolume(double value) {
        currentVolume = value;
        applyVolume(value);
    }

    private void applyVolume(double value) {
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

    public long getTime() {
        return mediaPlayer.status().time();
    }

    public long getLength() {
        return mediaPlayer.status().length();
    }

    public void seek(long timeMs) {
        mediaPlayer.controls().setTime(timeMs);
    }

    public boolean isPlayable() {
        State state = mediaPlayer.status().state();
        return state == State.PLAYING || state == State.PAUSED;
    }

    public void release() {
        mediaPlayer.release();
        factory.release();
    }
}
