package com.stellargear.cosmicplayer.ui;

import java.util.List;
import java.util.Random;

import com.stellargear.cosmicplayer.services.PlayerService.Song;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

public class SongList {

    private final ListView<Song> listView = new ListView<>();
    private boolean shuffleMode = false;

    public SongList() {
        listView.setCellFactory(lv -> new SongCell());
    }

    public Song getNext() {
        List<Song> items = listView.getItems();
        if (items.isEmpty()) return null;

        if (shuffleMode) {
            return items.get(getNextRandom(items));
        } else {
            return items.get(searchIndex(1, items));
        }
    }

    public Song getPrevious() {
        List<Song> items = listView.getItems();
        if (items.isEmpty()) return null;
        return items.get(searchIndex(-1, items));
    }

    public int getNextRandom(List<Song> list) {
        Random randi = new Random();
        int randomIndex = randi.nextInt(listView.getItems().size());
        return randomIndex;
    }

    public int searchIndex (int value, List<Song> list) {
        int currentIndex = listView.getSelectionModel().getSelectedIndex();
        int nextIndex = (currentIndex + value) % list.size();
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

    public ListView<Song>  getList () {
        return listView;
    }
}
