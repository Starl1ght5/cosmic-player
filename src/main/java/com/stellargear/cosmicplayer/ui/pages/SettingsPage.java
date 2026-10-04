package com.stellargear.cosmicplayer.ui.pages;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class SettingsPage {

    private final BorderPane root = new BorderPane();

    /*Button chooseFolderBtn = new Button("Cambiar carpeta");
    ///    chooseFolderBtn.setOnAction(e -> chooseFolder());

    public void chooseFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Selecciona tu carpeta de música");

        String saved = library.getMusicFolder();
        if (saved != null && new File(saved).isDirectory()) {
            chooser.setInitialDirectory(new File(saved));
        }

        File chosen = chooser.showDialog(owner);
        if (chosen != null) {
            library.loadFolder(chosen.getAbsolutePath());
        }
    }*/

    public Node getNode() { return root; }
}
