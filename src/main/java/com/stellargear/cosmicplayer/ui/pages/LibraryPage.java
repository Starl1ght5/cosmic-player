package com.stellargear.cosmicplayer.ui.pages;

import com.stellargear.cosmicplayer.ui.components.SongList;
import com.stellargear.cosmicplayer.viewmodels.LibraryViewModel;
import com.stellargear.cosmicplayer.viewmodels.PlaybackViewModel;
import com.stellargear.cosmicplayer.viewmodels.PlaylistViewModel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Window;

public class LibraryPage {

    private final BorderPane root = new BorderPane();
    private final LibraryViewModel library;
    private final Window owner;

    private final Label mainLabel = new Label("Biblioteca");

    public LibraryPage(LibraryViewModel library, PlaylistViewModel playlists,
                       PlaybackViewModel playback, Window owner) {
        this.library = library;
        this.owner = owner;

        SongList songList = new SongList(
                library.getSongs(),
                song -> playback.playFrom(library.getSongs(), song));

        root.setTop(new HBox(10, mainLabel));
        root.setCenter(songList.getNode());
        root.getStyleClass().add("library-page-main");
        mainLabel.getStyleClass().add("library-page-main-label");
    }

    public Node getNode() { return root; }
}