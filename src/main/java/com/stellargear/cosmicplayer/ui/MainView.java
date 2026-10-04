package com.stellargear.cosmicplayer.ui;

import com.stellargear.cosmicplayer.ui.components.SidePanel;
import com.stellargear.cosmicplayer.ui.components.PlayerToolbar;
import com.stellargear.cosmicplayer.ui.components.WindowBar;
import com.stellargear.cosmicplayer.ui.pages.LibraryPage;
import com.stellargear.cosmicplayer.ui.pages.SettingsPage;
import com.stellargear.cosmicplayer.viewmodels.LibraryViewModel;
import com.stellargear.cosmicplayer.viewmodels.PlaybackViewModel;
import com.stellargear.cosmicplayer.viewmodels.PlaylistViewModel;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.EnumMap;
import java.util.Map;

public class MainView {

    private final BorderPane root = new BorderPane();
    private final LibraryPage libraryPage;

    public MainView(PlaybackViewModel playback, LibraryViewModel library,
                    PlaylistViewModel playlists, Window owner) {

        WindowBar wb = new WindowBar((Stage) owner);
        libraryPage = new LibraryPage(library, playlists, playback, owner);

        Map<Sections, Node> pages = new EnumMap<>(Sections.class);
        pages.put(Sections.LIBRARY, libraryPage.getNode());
        ///pages.put(Sections.PLAYLISTS, new PlaylistPage(playlists, playback).getNode());
        pages.put(Sections.SETTINGS, new SettingsPage().getNode());

        SidePanel sidePanel = new SidePanel();
        sidePanel.selectedSectionProperty().addListener((obs, old, section) ->
                root.setCenter(pages.get(section)));

        root.setLeft(sidePanel.getNode());
        root.setBottom(new PlayerToolbar(playback).getNode());
        root.setCenter(pages.get(sidePanel.selectedSectionProperty().get()));
        root.setTop(wb.getNode());
    }

    public Parent getNode() { return root; }
}