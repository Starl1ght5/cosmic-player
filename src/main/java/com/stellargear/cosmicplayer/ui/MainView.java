package com.stellargear.cosmicplayer.ui;

import com.stellargear.cosmicplayer.ui.Sidepanel.SidePanel;
import com.stellargear.cosmicplayer.ui.Songlist.SongList;
import com.stellargear.cosmicplayer.ui.Toolbar.PlayerToolbar;
import com.stellargear.cosmicplayer.viewmodels.PlayerViewModel;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

public class MainView {

    private final BorderPane root = new BorderPane();
    private final SidePanel sidePanel;

    public MainView(PlayerViewModel vm, Window owner) {
        sidePanel = new SidePanel(vm, owner);
        root.setLeft(sidePanel.getNode());
        root.setCenter(new SongList(vm).getNode());
        root.setBottom(new PlayerToolbar(vm).getNode());
    }

    public Parent getNode() {
        return root;
    }

    public void askForMusicFolder() {
        sidePanel.chooseFolder();
    }
}