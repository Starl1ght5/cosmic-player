package com.stellargear.cosmicplayer.ui;

import java.io.File;

import com.stellargear.cosmicplayer.utils.Methods;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class SongListItem {

    private static final double COVER_SIZE = 40;

    private final Label songName = createLabel("song-name-label");
    private final Label artistName = createLabel("artist-name-label");
    private final Label albumName = createLabel("album-name-label");
    private final Label songDuration = new Label();

    private final ImageView coverArtBox = new ImageView();
    private final StackPane coverPane = new StackPane(coverArtBox);
    private final GridPane textGrid = new GridPane();
    private final BorderPane root = new BorderPane();

    public SongListItem(File songFile, String song, String artist, String album, String duration, byte[] imageData) {

        coverArtBox.setFitWidth(COVER_SIZE);
        coverArtBox.setFitHeight(COVER_SIZE);
        coverArtBox.setPreserveRatio(true);
        Rectangle clip = new Rectangle(COVER_SIZE, COVER_SIZE);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        coverArtBox.setClip(clip);

        coverPane.setMinSize(COVER_SIZE, COVER_SIZE);
        coverPane.setPrefSize(COVER_SIZE, COVER_SIZE);
        coverPane.setMaxSize(COVER_SIZE, COVER_SIZE);
        BorderPane.setAlignment(coverPane, Pos.CENTER);
        BorderPane.setMargin(coverPane, new Insets(0, 16, 0, 0));

        textGrid.getColumnConstraints().addAll(column(40), column(28), column(32));
        textGrid.setHgap(16);
        textGrid.setAlignment(Pos.CENTER_LEFT);
        textGrid.add(songName, 0, 0);
        textGrid.add(artistName, 1, 0);
        textGrid.add(albumName, 2, 0);
        textGrid.setMinWidth(0);

        songDuration.getStyleClass().add("duration-label");
        songDuration.setMinWidth(50);
        songDuration.setAlignment(Pos.CENTER_RIGHT);
        BorderPane.setAlignment(songDuration, Pos.CENTER_RIGHT);
        BorderPane.setMargin(songDuration, new Insets(0, 0, 0, 16));

        root.setLeft(coverPane);
        root.setCenter(textGrid);
        root.setRight(songDuration);
        root.setPadding(new Insets(8, 16, 8, 12));
        root.setMinWidth(0);
        root.getStyleClass().add("list-item");

        update(songFile, song, artist, album, duration, imageData);
    }

    private static ColumnConstraints column(double percent) {
        ColumnConstraints c = new ColumnConstraints();
        c.setPercentWidth(percent);
        c.setMinWidth(0);
        c.setHgrow(Priority.ALWAYS);
        return c;
    }

    private static Label createLabel(String styleClass) {
        Label l = new Label();
        l.getStyleClass().add(styleClass);
        l.setMinWidth(0);
        l.setMaxWidth(Double.MAX_VALUE);
        l.setTextOverrun(OverrunStyle.ELLIPSIS);
        return l;
    }

    public void update(File songFile, String song, String artist, String album, String duration, byte[] imageData) {
        songName.setText(song);
        artistName.setText(artist);
        albumName.setText(album);
        songDuration.setText(duration);
        songName.setTooltip(new Tooltip(song));
        artistName.setTooltip(new Tooltip(artist));
        albumName.setTooltip(new Tooltip(album));
        coverArtBox.setImage(Methods.toCachedThumbnail(songFile, imageData, 60));
    }

    public BorderPane getNode() {
        return root;
    }
}