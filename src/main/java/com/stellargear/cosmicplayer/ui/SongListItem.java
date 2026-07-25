package com.stellargear.cosmicplayer.ui;

import java.io.File;

import com.stellargear.cosmicplayer.utils.Methods;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;

public class SongListItem {

    private HBox songTimeBox;
    private HBox generalBox;

    private VBox controlBox;

    private Label songName = new Label("");
    private Label artistName = new Label("");
    private Label songDuration = new Label("");
    private Label albumName = new Label("");

    private final ImageView coverArtBox = new ImageView();

    public SongListItem (File songFile, String song, String artist, String album, String duration, byte[] imageData) {
        songTimeBox = new HBox(8, songName, songDuration);
        controlBox = new VBox(songTimeBox, artistName, albumName);
        generalBox = new HBox(10, coverArtBox, controlBox);

        coverArtBox.setFitWidth(50);
        coverArtBox.setFitHeight(50);
        coverArtBox.setPreserveRatio(true);

        generalBox.getStyleClass().add("list-item");
        albumName.getStyleClass().add("album-name-label");
        artistName.getStyleClass().add("artist-name-label");

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
        coverArtBox.setImage(Methods.toCachedThumbnail(songFile, imageData, 50));
    }

    public HBox getNode () {
        return generalBox;
    }
}
