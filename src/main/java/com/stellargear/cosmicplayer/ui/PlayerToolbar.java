package com.stellargear.cosmicplayer.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;

public class PlayerToolbar {

    private final ToolBar bar;
    private final Label songName = new Label("Song Name");
    private final Label artistName = new Label("Artist");
    private final Button playBtn = new Button("Reproducir");
    private final Button stopBtn = new Button("Detener");
    private final Slider volumeSlider = new Slider(0, 1, 1);

    public PlayerToolbar() {
        Label separator = new Label(" - ");
        bar = new ToolBar(
            songName,
            separator,
            artistName,
            playBtn,
            stopBtn,
            volumeSlider
        );
    }

    public ToolBar getNode() {
        return bar;
    }

    public Button getPlayBtn() {
        return playBtn;
    }

    public Button getStopBtn() {
        return stopBtn;
    }

    public Slider getVolumeSlider() {
        return volumeSlider;
    }

    public void setSongInfo(String title, String artist) {
        songName.setText(title);
        artistName.setText(artist);
    }
}
