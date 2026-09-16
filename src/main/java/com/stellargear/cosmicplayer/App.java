package com.stellargear.cosmicplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.stellargear.cosmicplayer.services.FileService;
import com.stellargear.cosmicplayer.services.PlayerService;
import com.stellargear.cosmicplayer.services.PlayerService.Song;
import com.stellargear.cosmicplayer.ui.PlayerToolbar;
import com.stellargear.cosmicplayer.services.SettingsService;
import com.stellargear.cosmicplayer.ui.SidePanel;
import com.stellargear.cosmicplayer.ui.SongList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.concurrent.Task;
import javafx.application.Platform;

import uk.co.caprica.vlcj.player.base.State;

public class App extends Application {

    PlayerService mediaPlayer = new PlayerService();
    FileService fileService = new FileService();
    PlayerToolbar playerToolbar = new PlayerToolbar();
    SettingsService settingsService = new SettingsService();
    SongList songList = new SongList();
    SidePanel sidePanel = new SidePanel();

    Stage primaryStage;

    private List<Song> currentPlaylist = new ArrayList<Song>();

    @Override
    public void start(Stage stage) {

        this.primaryStage = stage;

        BorderPane root = new BorderPane();
        root.setLeft(sidePanel.getNode());
        root.setCenter(songList.getNode());
        root.setBottom(playerToolbar.getNode());

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Cosmic Music Player");

        scene
            .getStylesheets()
            .add(getClass().getResource("/global.css").toExternalForm());

        stage.show();

        sidePanel.getChooseFolderBtn().setOnAction(e -> chooseFolderAndLoad());

        playerToolbar.getLastButton().setOnAction(e -> playPreviousSong());
        playerToolbar.getNextButton().setOnAction(e -> playNextSong());

        String savedFolder = settingsService.getMusicFolder();
        if (savedFolder != null) {
            loadSongsFrom(savedFolder);
        } else {
            chooseFolderAndLoad();
        }

        mediaPlayer.setOnEndReached(() -> Platform.runLater(this::playNextSong));

        playerToolbar.getPlayBtn().setOnAction(e -> {
            Song selected = songList.getSelected();
                if (selected != null) {
                    mediaPlayer.playOrResume(
                        selected.file(),
                        playerToolbar.getVolumeSlider().getValue()
                    );
                    playerToolbar.setSongInfo(selected.title(), selected.artist(), selected.coverArt());
                    updateToolbarButtons();
                }
        });

        songList.getList().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Song selected = songList.getSelected();
                if (selected != null) {
                    mediaPlayer.playOrResume(
                        selected.file(),
                        playerToolbar.getVolumeSlider().getValue()
                    );
                     playerToolbar.setSongInfo(selected.title(), selected.artist(), selected.coverArt());
                     updateToolbarButtons();

                     if (songList.getShuffleState()) {
                         generateRandomPlaylist();
                     }
                }
            }
        });

        playerToolbar
            .getShuffleBtn()
            .setOnAction(e -> {
                songList.setShuffle(playerToolbar.getShuffleBtn().isSelected());
                generateRandomPlaylist();
            }
        );

        playerToolbar
            .getVolumeSlider()
            .valueProperty()
            .addListener((obs, old, val) -> {
                mediaPlayer.changeVolume(val.doubleValue());
        });

        var progressSlider = playerToolbar.getProgressSlider();

        Timeline progressUpdater = new Timeline(
            new KeyFrame(Duration.millis(300), e -> {
                if (progressSlider.isValueChanging() || !mediaPlayer.isPlayable()) {
                    return;
                }

                long length = mediaPlayer.getLength();
                long time = mediaPlayer.getTime();

                if (length > 0) {
                    double pct = (time / (double) length) * 100.0;
                    progressSlider.setValue(pct);
                }

                playerToolbar.getCurrentTimeLabel().setText(formatTime(time));
                playerToolbar.getTotalTimeLabel().setText(formatTime(length));
            })
        );

        progressUpdater.setCycleCount(Timeline.INDEFINITE);
        progressUpdater.play();

        progressSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                long length = mediaPlayer.getLength();
                if (length > 0) {
                    long newTime = (long) ((progressSlider.getValue() / 100.0) * length);
                    mediaPlayer.seek(newTime);
                }
            }
        });

        progressSlider.setOnMouseReleased(e -> {
            long length = mediaPlayer.getLength();
            if (length > 0) {
                long newTime = (long) ((progressSlider.getValue() / 100.0) * length);
                mediaPlayer.seek(newTime);
            }
        });
    }

    private void playNextSong() {
        Song next;

        if (songList.getShuffleState()) {
            next = songList.getNext(currentPlaylist);
        } else {
            next = songList.getNext(songList.getItems());
        }
        if (next == null) return;

        songList.select(next);
        mediaPlayer.playOrResume(next.file(), playerToolbar.getVolumeSlider().getValue());
        playerToolbar.setSongInfo(next.title(), next.artist(), next.coverArt());
    }

    private void playPreviousSong() {
        Song last;

        if (songList.getShuffleState()) {
            last = songList.getPrevious(currentPlaylist);
        } else {
            last = songList.getPrevious(songList.getItems());
        }

        if (last == null) return;

        songList.select(last);
        mediaPlayer.playOrResume(last.file(), playerToolbar.getVolumeSlider().getValue());
        playerToolbar.setSongInfo(last.title(), last.artist(), last.coverArt());
    }

    private static String formatTime(long ms) {
        if (ms < 0) return "00:00";
        long totalSeconds = ms / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
        public void stop() {
            mediaPlayer.release();
    }

    public static void main(String[] args) {
        launch(args);
    }


    private void chooseFolderAndLoad() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Selecciona tu carpeta de música");

        String saved = settingsService.getMusicFolder();
        if (saved != null && new File(saved).isDirectory()) {
            chooser.setInitialDirectory(new File(saved));
        }

        File chosen = chooser.showDialog(primaryStage);
        if (chosen != null) {
            settingsService.setMusicFolder(chosen.getAbsolutePath());
            loadSongsFrom(chosen.getAbsolutePath());
        }
    }

    private void loadSongsFrom(String folderPath) {
        Task<List<Song>> loadSongsTask = new Task<>() {
            @Override
            protected List<Song> call() {
                fileService.getSongs(folderPath);
                return fileService.returnSongList();
            }
        };

        loadSongsTask.setOnSucceeded(e -> {
            songList.setItems(loadSongsTask.getValue());
        });

        loadSongsTask.setOnFailed(e -> {
            loadSongsTask.getException().printStackTrace();
        });

        Thread thread = new Thread(loadSongsTask);
        thread.setDaemon(true);
        thread.start();
    }

    public void updateToolbarButtons () {
        State status = mediaPlayer.getPlayingState();

        switch (status) {
            case PLAYING:
                playerToolbar.changeIsPlaying(true);
                break;
            case OPENING:
                playerToolbar.changeIsPlaying(true);
                break;
            case PAUSED:
                playerToolbar.changeIsPlaying(false);
                break;
            default:
                break;
        }
    }

    public void generateRandomPlaylist () {
        ArrayList<Song> base = fileService.returnArrayList();
        int total = base.size();
        List<Song> mix = new ArrayList<Song>();
        Random randi = new Random();

        for (int i = 0; i < total; i++) {
            int index = randi.nextInt(base.size());
            mix.add(base.get(index));
            base.remove(index);
        }

        currentPlaylist = mix;
    }
}
