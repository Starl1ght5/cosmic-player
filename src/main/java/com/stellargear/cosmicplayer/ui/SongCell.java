package com.stellargear.cosmicplayer.ui;

import com.stellargear.cosmicplayer.services.PlayerService.Song;

import javafx.scene.control.ListCell;

public class SongCell extends ListCell<Song> {
    private SongListItem item;

    @Override
    protected void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);
    
        if (empty || song == null) {
            setText(null);
            setGraphic(null);
            return;
        }
    
        if (item == null) {
            item = new SongListItem(song.file(), song.title(), song.artist(), song.duration(), song.coverArt());
            setGraphic(item.getNode());
        } else {
            item.update(song.file(), song.title(), song.artist(), song.duration(), song.coverArt());
        }
    }
}
