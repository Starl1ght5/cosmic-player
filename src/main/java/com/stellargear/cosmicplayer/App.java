package com.stellargear.cosmicplayer;

import com.stellargear.cosmicplayer.services.FileService;
import com.stellargear.cosmicplayer.services.PlayerService;
import com.stellargear.cosmicplayer.ui.PlayerToolbar;
import com.stellargear.cosmicplayer.ui.SongList;
import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    PlayerService mediaPlayer = new PlayerService();
    FileService fileService = new FileService();
    PlayerToolbar playerToolbar = new PlayerToolbar();
    SongList songList = new SongList();

    @Override
    public void start(Stage stage) {
        File folder = new File("/home/dimewolf/Música/");
        songList.setItems(fileService.getSongNames("/home/dimewolf/Música/"));

        playerToolbar.getPlayBtn().setOnAction(e -> {
            String selected = songList.getSelected();
            if (selected != null) mediaPlayer.playSong(
                new File(folder, selected),
                playerToolbar.getVolumeSlider().getValue()
            );
        });

        playerToolbar.getStopBtn().setOnAction(e -> mediaPlayer.pause());

        playerToolbar
            .getVolumeSlider()
            .valueProperty()
            .addListener((obs, old, val) -> {
                mediaPlayer.changeVolume(val.doubleValue());
            });

        songList.selectedSongProperty().addListener((obs, old, nuevo) -> {
            if (nuevo == null) return;
            File song = new File(folder, nuevo);
            var meta = fileService.readMetadata(song);
            playerToolbar.setSongInfo(meta.title(), meta.artist());
        });

        BorderPane root = new BorderPane();
        root.setCenter(songList.getNode());
        root.setBottom(playerToolbar.getNode());

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Cosmic Music Player");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
