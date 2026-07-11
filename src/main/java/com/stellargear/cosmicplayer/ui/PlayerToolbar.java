package com.stellargear.cosmicplayer.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.ByteArrayInputStream;

public class PlayerToolbar {

    private final GridPane bar;
    private final Label songName = new Label("Song Name");
    private final Label artistName = new Label("Artist");
    private final Button playBtn = new Button("Reproducir");
    private final Slider volumeSlider = new Slider(0, 1, 1);
    private final VBox songBox = new VBox(songName, artistName);
    private final ImageView coverArtBox = new ImageView();

    private final Slider progressSlider = new Slider(0, 100, 0);
    private final Label currentTimeLabel = new Label("00:00");
    private final Label totalTimeLabel = new Label("00:00");

    public PlayerToolbar() {
        HBox leftBox = new HBox(8, coverArtBox, songBox);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        HBox centerBox = new HBox(playBtn);
        centerBox.setAlignment(Pos.CENTER);
        HBox rightBox = new HBox(volumeSlider);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        progressSlider.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressSlider, Priority.ALWAYS);
        HBox progressBox = new HBox(8, currentTimeLabel, progressSlider, totalTimeLabel);
        progressBox.setAlignment(Pos.CENTER);

        bar = new GridPane();

        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(35);
        ColumnConstraints center = new ColumnConstraints();
        center.setPercentWidth(30);
        ColumnConstraints right = new ColumnConstraints();
        right.setPercentWidth(35);
        bar.getColumnConstraints().addAll(left, center, right);

        bar.add(progressBox, 0, 0, 3, 1);
        bar.add(leftBox, 0, 1);
        bar.add(centerBox, 1, 1);
        bar.add(rightBox, 2, 1);

        coverArtBox.setFitWidth(80);
        coverArtBox.setFitHeight(80);
        coverArtBox.setPreserveRatio(true);

        GridPane.setHgrow(leftBox, Priority.ALWAYS);
        GridPane.setHgrow(centerBox, Priority.ALWAYS);
        GridPane.setHgrow(rightBox, Priority.ALWAYS);
        GridPane.setHgrow(progressBox, Priority.ALWAYS);

        leftBox.setMaxWidth(Double.MAX_VALUE);
        centerBox.setMaxWidth(Double.MAX_VALUE);
        rightBox.setMaxWidth(Double.MAX_VALUE);

        songBox.getStyleClass().add("song-box");
        songName.getStyleClass().add("song-name");
        bar.getStyleClass().add("player-toolbar");
        coverArtBox.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        progressBox.getStyleClass().add("progress-box");
    }

    public GridPane getNode() {
        return bar;
    }

    public Button getPlayBtn() {
        return playBtn;
    }

    public Label getSongLabel() {
        return songName;
    }

    public Slider getVolumeSlider() {
        return volumeSlider;
    }

    public Slider getProgressSlider() {
            return progressSlider;
    }

    public Label getCurrentTimeLabel() {
        return currentTimeLabel;
    }

    public Label getTotalTimeLabel() {
        return totalTimeLabel;
    }

    public void setSongInfo(String title, String artist, byte[] coverArt) {
        songName.setText(title);
        artistName.setText(artist);
        coverArtBox.setImage(toImage(coverArt));
    }

    Image toImage(byte[] coverArtBytes) {
        if (coverArtBytes == null || coverArtBytes.length == 0) {
            return new Image(getClass().getResourceAsStream("/images/default_cover.png"));
        }
        return new Image(new ByteArrayInputStream(coverArtBytes));
    }
}
