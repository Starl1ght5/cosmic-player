package com.stellargear.cosmicplayer.ui;

import java.util.List;

import com.stellargear.cosmicplayer.services.PlayerService.Song;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

public class SongList {

    private final ListView<Song> listView = new ListView<>();

        public SongList() {
             listView.setCellFactory(lv -> new SongCell());
        }

        public ListView<Song> getNode() {
            return listView;
        }

        public void setItems(List<Song> items) {
            listView.setItems(FXCollections.observableArrayList(items));
        }

        public ReadOnlyObjectProperty<Song> selectedSongProperty() {
            return listView.getSelectionModel().selectedItemProperty();
        }

        public Song getSelected() {
            return listView.getSelectionModel().getSelectedItem();
        }
}
