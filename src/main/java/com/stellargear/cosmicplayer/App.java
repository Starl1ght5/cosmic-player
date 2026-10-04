package com.stellargear.cosmicplayer;

import com.stellargear.cosmicplayer.services.*;

import com.stellargear.cosmicplayer.services.persistence.PlaylistStorage;
import com.stellargear.cosmicplayer.services.persistence.SettingsService;
import com.stellargear.cosmicplayer.ui.MainView;
import com.stellargear.cosmicplayer.viewmodels.LibraryViewModel;
import com.stellargear.cosmicplayer.viewmodels.PlaybackViewModel;
import com.stellargear.cosmicplayer.viewmodels.PlaylistViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class App extends Application {

    private PlaybackViewModel viewModel;

    @Override
    public void start(Stage stage) {
        PlayerService player = new PlayerService();
        MainView view = getMainView(stage, player);

        Scene scene = new Scene(view.getNode(), 1024, 640);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/global.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Cosmic Music Player");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    private static MainView getMainView(Stage stage, PlayerService player) {
        LibraryService libraryService = new LibraryService(new MetadataReader());
        PlaylistService playlistService = new PlaylistService(new PlaylistStorage());

        PlaybackViewModel playback = new PlaybackViewModel(player, new PlaybackQueue(), libraryService);
        LibraryViewModel library = new LibraryViewModel(libraryService, new SettingsService());
        PlaylistViewModel playlists = new PlaylistViewModel(playlistService, libraryService);

        return new MainView(playback, library, playlists, stage);
    }

    @Override
    public void stop() {
        viewModel.dispose();
    }

    public static void main(String[] args) {
        launch(args);
    }
}