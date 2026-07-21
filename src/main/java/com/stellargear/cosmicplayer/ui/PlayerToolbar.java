package com.stellargear.cosmicplayer.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.ByteArrayInputStream;

public class PlayerToolbar {

    private final GridPane bar = new GridPane();

    private final Label songName = new Label("Song Name");
    private final Label artistName = new Label("Artist");
    private final Label currentTimeLabel = new Label("00:00");
    private final Label totalTimeLabel = new Label("00:00");

    private final Button playBtn = new Button();
    private final Button nextBtn = new Button();
    private final Button lastBtn = new Button();

    private final ImageView coverArtBox = new ImageView();

    private final ToggleButton shuffleBtn = new ToggleButton();

    private final Slider progressSlider = new Slider(0, 100, 0);
    private final Slider volumeSlider = new Slider(0, 1, 1);

    private final VBox songBox = new VBox(songName, artistName);

    private final HBox leftBox = new HBox(8, coverArtBox, songBox);
    private final HBox playerBox = new HBox(4, lastBtn, playBtn, nextBtn);
    private final VBox rightBox = new VBox(4, volumeSlider, shuffleBtn);
    private final HBox progressBox = new HBox(8, currentTimeLabel, progressSlider, totalTimeLabel);

    private final ImageView playBtnImgView = new ImageView();

    private boolean isPlaying = false;

    public PlayerToolbar() {
        VBox centerBox = new VBox(3, progressBox, playerBox);

        leftBox.setAlignment(Pos.CENTER_LEFT);
        playerBox.setAlignment(Pos.CENTER);
        rightBox.setAlignment(Pos.CENTER);
        songBox.setAlignment(Pos.CENTER_LEFT);

        progressSlider.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(30);
        ColumnConstraints center = new ColumnConstraints();
        center.setPercentWidth(40);
        ColumnConstraints right = new ColumnConstraints();
        right.setPercentWidth(30);
        bar.getColumnConstraints().addAll(left, center, right);

        bar.add(leftBox, 0, 1);
        bar.add(centerBox, 1, 1);
        bar.add(rightBox, 2, 1);

        coverArtBox.setFitWidth(80);
        coverArtBox.setFitHeight(80);
        coverArtBox.setPreserveRatio(true);

        HBox.setHgrow(progressSlider, Priority.ALWAYS);
        GridPane.setHgrow(leftBox, Priority.ALWAYS);
        GridPane.setHgrow(centerBox, Priority.ALWAYS);
        GridPane.setHgrow(rightBox, Priority.ALWAYS);
        GridPane.setHgrow(progressBox, Priority.ALWAYS);

        leftBox.setMaxWidth(Double.MAX_VALUE);
        centerBox.setMaxWidth(Double.MAX_VALUE);
        rightBox.setMaxWidth(Double.MAX_VALUE);

        Image playBtnImg = new Image(getClass().getResourceAsStream("/icons/IcRoundPlayArrow.png"));
        playBtnImgView.setImage(playBtnImg);
        playBtnImgView.setFitHeight(40);
        playBtnImgView.setFitWidth(40);
        playBtnImgView.setPreserveRatio(true);
        playBtn.setGraphic(playBtnImgView);

        Image nextBtnImg = new Image(getClass().getResourceAsStream("/icons/IcRoundSkipNext.png"));
        ImageView nextBtnImgView = new ImageView(nextBtnImg);
        nextBtnImgView.setFitHeight(30);
        nextBtnImgView.setFitWidth(30);
        nextBtnImgView.setPreserveRatio(true);
        nextBtn.setGraphic(nextBtnImgView);

        Image lastBtnImg = new Image(getClass().getResourceAsStream("/icons/IcRoundSkipPrevious.png"));
        ImageView lastBtnImgView = new ImageView(lastBtnImg);
        lastBtnImgView.setFitHeight(30);
        lastBtnImgView.setFitWidth(30);
        lastBtnImgView.setPreserveRatio(true);
        lastBtn.setGraphic(lastBtnImgView);

        Image shuffleBtnImg = new Image(getClass().getResourceAsStream("/icons/IcOutlineShuffle.png"));
        ImageView shuffleBtnImgView = new ImageView(shuffleBtnImg);
        shuffleBtnImgView.setFitHeight(30);
        shuffleBtnImgView.setFitWidth(30);
        shuffleBtnImgView.setPreserveRatio(true);
        shuffleBtn.setGraphic(shuffleBtnImgView);

        songBox.getStyleClass().add("song-box");
        songName.getStyleClass().add("song-name");
        bar.getStyleClass().add("player-toolbar");
        coverArtBox.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        progressBox.getStyleClass().add("progress-box");
        playBtn.getStyleClass().add("play-button");
        shuffleBtn.getStyleClass().add("shuffle-button");
        nextBtn.getStyleClass().add("next-button");
        lastBtn.getStyleClass().add("last-button");
        volumeSlider.getStyleClass().add("volume-slider");
    }

    public void changePlayButton () {
        if (isPlaying) {
            Image playBtnImg = new Image(getClass().getResourceAsStream("/icons/IcRoundPause.png"));
            playBtnImgView.setImage(playBtnImg);
        } else {
            Image playBtnImg = new Image(getClass().getResourceAsStream("/icons/IcRoundPlayArrow.png"));
            playBtnImgView.setImage(playBtnImg);
        }
    }

    Image toImage(byte[] coverArtBytes) {
        if (coverArtBytes == null || coverArtBytes.length == 0) {
            return new Image(getClass().getResourceAsStream("/images/default_cover.png"));
        }
        return new Image(new ByteArrayInputStream(coverArtBytes));
    }

    /// Setters
    public void setSongInfo(String title, String artist, byte[] coverArt) {
        songName.setText(title);
        artistName.setText(artist);
        coverArtBox.setImage(toImage(coverArt));
    }

    public void changeIsPlaying (boolean value) {
        isPlaying = value;
        changePlayButton();
    }

    /// Getters
    public ToggleButton getShuffleBtn () {
        return shuffleBtn;
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

    public Button getLastButton () {
        return lastBtn;
    }

    public Button getNextButton () {
        return nextBtn;
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

}
