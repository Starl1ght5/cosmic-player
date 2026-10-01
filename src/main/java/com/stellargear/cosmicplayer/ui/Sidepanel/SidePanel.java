package com.stellargear.cosmicplayer.ui.Sidepanel;

import com.stellargear.cosmicplayer.viewmodels.PlayerViewModel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;

public class SidePanel {

    private final VBox layout = new VBox();
    private final Button chooseFolderBtn = new Button("Elegir carpeta");
    private final PlayerViewModel vm;
    private final Window owner;

    public SidePanel(PlayerViewModel vm, Window owner) {
        this.vm = vm;
        this.owner = owner;
        layout.getChildren().addAll(new Label("Home"), chooseFolderBtn);
        chooseFolderBtn.setOnAction(e -> chooseFolder());
    }

    public void chooseFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Selecciona tu carpeta de música");

        String saved = vm.getMusicFolder();
        if (saved != null && new File(saved).isDirectory()) {
            chooser.setInitialDirectory(new File(saved));
        }

        File chosen = chooser.showDialog(owner);
        if (chosen != null) {
            vm.loadFolder(chosen.getAbsolutePath());
        }
    }

    public VBox getNode() {
        return layout;
    }
}