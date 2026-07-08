package com.stellargear.cosmicplayer.ui;

import java.util.List;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

public class SongList {

    private final ListView<String> listView = new ListView<>();
    
        public SongList() {
        }
    
        public ListView<String> getNode() {
            return listView;
        }
    
        public void setItems(List<String> items) {
            listView.setItems(FXCollections.observableArrayList(items));
        }
    
        public ReadOnlyObjectProperty<String> selectedSongProperty() {
            return listView.getSelectionModel().selectedItemProperty();
        }
    
        public String getSelected() {
            return listView.getSelectionModel().getSelectedItem();
        }
}
