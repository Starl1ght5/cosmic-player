package com.stellargear.cosmicplayer;

import com.stellargear.cosmicplayer.services.*;

import com.stellargear.cosmicplayer.ui.MainView;
import com.stellargear.cosmicplayer.viewmodels.PlayerViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    private PlayerViewModel viewModel;

    @Override
    public void start(Stage stage) {
        viewModel = new PlayerViewModel(
                new PlayerService(),
                new PlaybackQueue(),
                new LibraryService(new MetadataReader()),
                new SettingsService()
        );

        MainView view = new MainView(viewModel, stage);

        Scene scene = new Scene(view.getNode(), 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/global.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Cosmic Music Player");
        stage.show();

        if (!viewModel.loadSavedFolder()) {
            view.askForMusicFolder();
        }
    }

    @Override
    public void stop() {
        viewModel.dispose();
    }

    public static void main(String[] args) {
        launch(args);
    }
}