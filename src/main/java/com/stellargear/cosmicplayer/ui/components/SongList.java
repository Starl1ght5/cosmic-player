package com.stellargear.cosmicplayer.ui.components;


import com.stellargear.cosmicplayer.models.Song;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;

import java.util.function.Consumer;

public class SongList {

    private final ListView<Song> listView = new ListView<>();

    public SongList(ObservableList<Song> songs, Consumer<Song> onPlay) {
        listView.setItems(songs);
        listView.setCellFactory(lv -> new SongCell());
        listView.getStyleClass().add("list-view");

        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && getSelected() != null) {
                onPlay.accept(getSelected());
            }
        });
    }

    public Song getSelected() { return listView.getSelectionModel().getSelectedItem(); }

    public void setContextMenu(ContextMenu menu) { listView.setContextMenu(menu); }

    public ListView<Song> getNode() { return listView; }
}
