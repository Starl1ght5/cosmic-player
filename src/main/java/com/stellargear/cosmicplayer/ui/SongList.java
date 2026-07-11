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

        public Song getNext() {
            List<Song> items = listView.getItems();
            if (items.isEmpty()) return null;

            int currentIndex = listView.getSelectionModel().getSelectedIndex();
            int nextIndex = (currentIndex + 1) % items.size();
            return items.get(nextIndex);
        }

        public Song getPrevious() {
            List<Song> items = listView.getItems();
            if (items.isEmpty()) return null;

            int currentIndex = listView.getSelectionModel().getSelectedIndex();
            int previousIndex = (currentIndex - 1) % items.size();
            return items.get(previousIndex);
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

        public void select(Song song) {
            listView.getSelectionModel().select(song);
        }
}
