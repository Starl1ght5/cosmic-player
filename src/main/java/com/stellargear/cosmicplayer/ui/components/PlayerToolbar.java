package com.stellargear.cosmicplayer.ui.components;

import com.stellargear.cosmicplayer.viewmodels.PlaybackViewModel;
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
import javafx.scene.shape.Rectangle;

import com.stellargear.cosmicplayer.utils.Methods;

import java.util.Objects;

/**
 * Playback control bar shown at the bottom of the main window.
 *
 * <p>Displays the current song info (cover art, title, artist), playback
 * controls (previous, play/pause, next), a seek slider with time labels,
 * a volume slider, and a shuffle toggle.</p>
 *
 * <p>All UI state is driven by the given {@link PlaybackViewModel}: user
 * actions call the view model, and view model changes update the UI
 * automatically through bindings and listeners.</p>
 *
 * @author Starl1ght5
 * @since 1.0
 */
public class PlayerToolbar {

    private final GridPane bar = new GridPane();

    private final Label songName = new Label("");
    private final Label artistName = new Label("");
    private final Label currentTimeLabel = new Label("");
    private final Label totalTimeLabel = new Label("");

    private final Button playBtn = new Button();
    private final Button nextBtn = new Button();
    private final Button lastBtn = new Button();

    private final Image playIcon  = loadIcon("IcRoundPlayArrow");
    private final Image pauseIcon = loadIcon("IcRoundPause");
    private final Image nextIcon  = loadIcon("IcRoundSkipNext");
    private final Image prevIcon  = loadIcon("IcRoundSkipPrevious");
    private final Image shuffleIcon = loadIcon("IcOutlineShuffle");

    private final ImageView coverArtBox = new ImageView();

    private final ToggleButton shuffleBtn = new ToggleButton();

    private final Slider progressSlider = new Slider(0, 1, 0);
    private final Slider volumeSlider = new Slider(0, 1, 1);

    private final VBox songBox = new VBox(songName, artistName);

    private final HBox leftBox = new HBox(8, coverArtBox, songBox);
    private final HBox playerBox = new HBox(4, lastBtn, playBtn, nextBtn);
    private final VBox rightBox = new VBox(4, volumeSlider, shuffleBtn);
    private final HBox progressBox = new HBox(8, currentTimeLabel, progressSlider, totalTimeLabel);

    private final ImageView playBtnImgView = new ImageView();

    /**
     * Builds the toolbar and binds it to the given view model.
     *
     * @param vm the view model that drives the UI state
     */
    public PlayerToolbar(PlaybackViewModel vm) {
        bind(vm);

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

        Rectangle clip = new Rectangle(coverArtBox.getFitWidth(), coverArtBox.getFitHeight());
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        coverArtBox.setClip(clip);

        HBox.setHgrow(progressSlider, Priority.ALWAYS);
        GridPane.setHgrow(leftBox, Priority.ALWAYS);
        GridPane.setHgrow(centerBox, Priority.ALWAYS);
        GridPane.setHgrow(rightBox, Priority.ALWAYS);
        GridPane.setHgrow(progressBox, Priority.ALWAYS);

        leftBox.setMaxWidth(Double.MAX_VALUE);
        centerBox.setMaxWidth(Double.MAX_VALUE);
        rightBox.setMaxWidth(Double.MAX_VALUE);

        playBtnImgView.setImage(playIcon);
        playBtnImgView.setFitHeight(40);
        playBtnImgView.setFitWidth(40);
        playBtnImgView.setPreserveRatio(true);
        playBtn.setGraphic(playBtnImgView);

        ImageView nextBtnImgView = new ImageView(nextIcon);
        nextBtnImgView.setFitHeight(30);
        nextBtnImgView.setFitWidth(30);
        nextBtnImgView.setPreserveRatio(true);
        nextBtn.setGraphic(nextBtnImgView);

        ImageView lastBtnImgView = new ImageView(prevIcon);
        lastBtnImgView.setFitHeight(30);
        lastBtnImgView.setFitWidth(30);
        lastBtnImgView.setPreserveRatio(true);
        lastBtn.setGraphic(lastBtnImgView);

        ImageView shuffleBtnImgView = new ImageView(shuffleIcon);
        shuffleBtnImgView.setFitHeight(30);
        shuffleBtnImgView.setFitWidth(30);
        shuffleBtnImgView.setPreserveRatio(true);
        shuffleBtn.setGraphic(shuffleBtnImgView);

        songBox.getStyleClass().add("song-box");
        songName.getStyleClass().add("song-name");
        bar.getStyleClass().add("player-toolbar");
        progressBox.getStyleClass().add("progress-box");
        playBtn.getStyleClass().add("play-button");
        shuffleBtn.getStyleClass().add("shuffle-button");
        nextBtn.getStyleClass().add("next-button");
        lastBtn.getStyleClass().add("last-button");
        volumeSlider.getStyleClass().add("volume-slider");
    }

    /**
     * Connects UI controls to the view model: buttons invoke view model
     * commands, sliders and toggles are bidirectionally bound, and labels
     * and icons update automatically when the view model state changes.
     */
    private void bind(PlaybackViewModel vm) {
        playBtn.setOnAction(e -> vm.togglePlay());
        nextBtn.setOnAction(e -> vm.next());
        lastBtn.setOnAction(e -> vm.previous());

        shuffleBtn.selectedProperty().bindBidirectional(vm.shuffleProperty());
        volumeSlider.valueProperty().bindBidirectional(vm.volumeProperty());

        songName.textProperty().bind(vm.titleProperty());
        artistName.textProperty().bind(vm.artistProperty());
        currentTimeLabel.textProperty().bind(vm.currentTimeTextProperty());
        totalTimeLabel.textProperty().bind(vm.totalTimeTextProperty());

        vm.currentSongProperty().addListener((obs, old, song) ->
                coverArtBox.setImage(Methods.toImage(song == null ? null : song.coverArt(), 80)));

        vm.playingProperty().addListener((obs, old, isPlaying) ->
                playBtnImgView.setImage(isPlaying ? pauseIcon : playIcon));

        // Avoid fighting the user while they drag the slider
        vm.progressProperty().addListener((obs, old, p) -> {
            if (!progressSlider.isValueChanging()) {
                progressSlider.setValue(p.doubleValue());
            }
        });

        progressSlider.setOnMouseReleased(e -> vm.seek(progressSlider.getValue()));
    }

    private static Image loadIcon(String name) {
        return new Image(Objects.requireNonNull(
                PlayerToolbar.class.getResourceAsStream("/icons/" + name + ".png")));
    }

    /**
     * Returns the root node of this component, ready to be added to a scene.
     *
     * @return the toolbar's root node
     */
    public GridPane getNode() {
        return bar;
    }
}