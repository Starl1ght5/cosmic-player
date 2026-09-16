package com.stellargear.cosmicplayer.ui;

import java.io.File;

import com.stellargear.cosmicplayer.utils.Methods;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;

public class SongListItem {

    private Label songName = new Label("");
    private Label artistName = new Label("");
    private Label songDuration = new Label("");
    private Label albumName = new Label("");

    private GridPane generalBox = new GridPane();

    private final ImageView coverArtBox = new ImageView();

    private final  HBox imageBox = new HBox(coverArtBox);
    private final HBox extraBox = new HBox(songDuration);

    private final HBox controlBox = new HBox(30, songName, artistName, albumName, extraBox);

    public SongListItem (File songFile, String song, String artist, String album, String duration, byte[] imageData) {

        coverArtBox.setFitWidth(40);
        coverArtBox.setFitHeight(40);
        coverArtBox.setPreserveRatio(true);
        coverArtBox.setLayoutY(40);
        coverArtBox.setLayoutX(40);
        
        imageBox.setAlignment(Pos.CENTER_LEFT);
        generalBox.setAlignment(Pos.CENTER);
        extraBox.setAlignment(Pos.CENTER_RIGHT);

        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(8);
        ColumnConstraints center = new ColumnConstraints();
        center.setPercentWidth(89);
        center.setMinWidth(0);
        ColumnConstraints right = new ColumnConstraints();
        right.setPercentWidth(3);
        generalBox.getColumnConstraints().addAll(left, center, right);
        generalBox.setMinWidth(0);

        generalBox.add(imageBox, 0, 0);
        generalBox.add(controlBox, 1, 0);
        generalBox.add(extraBox, 1, 0);

        generalBox.getStyleClass().add("list-item");
        albumName.getStyleClass().add("album-name-label");
        artistName.getStyleClass().add("artist-name-label");
        controlBox.getStyleClass().add("control-box");

        Rectangle clip = new Rectangle(coverArtBox.getFitWidth(), coverArtBox.getFitHeight());
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        coverArtBox.setClip(clip);

        update(songFile, song, artist, album, duration, imageData);
    }

    public void update(File songFile, String song, String artist, String album, String duration, byte[] imageData) {
        songName.setText(song);
        artistName.setText(artist);
        songDuration.setText(duration);
        albumName.setText(album);
        coverArtBox.setImage(Methods.toCachedThumbnail(songFile, imageData, 60));
    }

    public GridPane getNode () {
        return generalBox;
    }
}
