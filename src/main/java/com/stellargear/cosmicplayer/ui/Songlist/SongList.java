package com.stellargear.cosmicplayer.ui.Songlist;


import com.stellargear.cosmicplayer.models.Song;

import com.stellargear.cosmicplayer.viewmodels.PlayerViewModel;
import javafx.scene.control.ListView;

public class SongList {

    private final ListView<Song> listView = new ListView<>();

    public SongList(PlayerViewModel vm) {
        listView.setItems(vm.getSongs());
        listView.setCellFactory(lv -> new SongCell());
        listView.getStyleClass().add("list-view");

        vm.selectedSongProperty().bind(listView.getSelectionModel().selectedItemProperty());

        vm.currentSongProperty().addListener((obs, old, song) ->
                listView.getSelectionModel().select(song));

        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                vm.play(listView.getSelectionModel().getSelectedItem());
            }
        });
    }

    public ListView<Song> getNode() {
        return listView;
    }
}
