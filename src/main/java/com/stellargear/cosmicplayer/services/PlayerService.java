package com.stellargear.cosmicplayer.services;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PlayerService {

    private MediaPlayer mediaPlayer;

    public void playSong(File song) {
        if (
            mediaPlayer != null &&
            mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED
        ) {
            mediaPlayer.play();
            return;
        }

        if (mediaPlayer != null) mediaPlayer.stop();
        Media media = new Media(song.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public void pause() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null) mediaPlayer.stop();
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) mediaPlayer.setVolume(volume);
    }

    public MediaPlayer.Status getStatus() {
        return mediaPlayer != null ? mediaPlayer.getStatus() : null;
    }
}
