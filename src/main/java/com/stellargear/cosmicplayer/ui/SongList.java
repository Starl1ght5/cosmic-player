package com.stellargear.cosmicplayer.ui;

import java.util.List;

import com.stellargear.cosmicplayer.services.PlayerService.Song;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

public class SongList {

    private final ListView<Song> listView = new ListView<>();
    private boolean shuffleMode = false;

    public SongList() {
        listView.setCellFactory(lv -> new SongCell());

        listView.getStyleClass().add("list-view");
    }

    public Song getNext(List<Song> list) {
        if (list.isEmpty()) return null;
        return list.get(searchIndex(1, list));
    }

    public Song getPrevious(List<Song> list) {
        if (list.isEmpty()) return null;
        return list.get(searchIndex(-1, list));
    }

    public int searchIndex (int value, List<Song> list) {
        Song current = getSelected();
        int currentIndex = list.indexOf(current);
        
        if (currentIndex == -1) {
            return value >= 0 ? 0 : list.size() - 1;
        }

        int size = list.size();
        int nextIndex = ((currentIndex + value) % size + size) % size;
        return nextIndex;
    }

    /// Setters
    public void setItems(List<Song> items) {
        listView.setItems(FXCollections.observableArrayList(items));
    }

    public void setShuffle (boolean value) {
        shuffleMode = value;
    }

    /// Getters
    public ReadOnlyObjectProperty<Song> selectedSongProperty() {
        return listView.getSelectionModel().selectedItemProperty();
    }

    public void select(Song song) {
        listView.getSelectionModel().select(song);
    }

    public Boolean getShuffleState () {
        return shuffleMode;
    }

    public Song getSelected() {
        return listView.getSelectionModel().getSelectedItem();
    }

    public ListView<Song> getNode() {
        return listView;
    }

    public ListView<Song> getList () {
        return listView;
    }

    public List<Song> getItems () {
        return listView.getItems();
    }
}
