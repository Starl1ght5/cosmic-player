package com.stellargear.cosmicplayer.ui;

import java.io.File;

import com.stellargear.cosmicplayer.utils.Methods;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;

public class SongListItem {

    private Label songName = new Label("");
    private Label artistName = new Label("");
    private Label songDuration = new Label("");
    private Label albumName = new Label("");

    private GridPane generalBox = new GridPane();

    private final ImageView coverArtBox = new ImageView();

    private final HBox songNameBox = new HBox(8, songName);
    private final  HBox imageBox = new HBox(coverArtBox);
    private final HBox extraBox = new HBox(songDuration);

    private final VBox controlBox = new VBox(songNameBox, artistName, albumName);
    
    public SongListItem (File songFile, String song, String artist, String album, String duration, byte[] imageData) {
        
        coverArtBox.setFitWidth(60);
        coverArtBox.setFitHeight(60);
        coverArtBox.setPreserveRatio(true);

        imageBox.setAlignment(Pos.CENTER);
        extraBox.setAlignment(Pos.CENTER_RIGHT);

        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(10);
        ColumnConstraints center = new ColumnConstraints();
        center.setPercentWidth(80);
        center.setMinWidth(0);
        ColumnConstraints right = new ColumnConstraints();
        right.setPercentWidth(10);
        generalBox.getColumnConstraints().addAll(left, center, right);
        generalBox.setMinWidth(0);

        generalBox.add(imageBox, 0, 0);
        generalBox.add(controlBox, 1, 0);
        generalBox.add(extraBox, 2, 0);

        generalBox.getStyleClass().add("list-item");
        albumName.getStyleClass().add("album-name-label");
        artistName.getStyleClass().add("artist-name-label");
        controlBox.getStyleClass().add("control-box");

        Rectangle clip = new Rectangle(coverArtBox.getFitWidth(), coverArtBox.getFitHeight());
        clip.setArcWidth(30);
        clip.setArcHeight(30);
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
