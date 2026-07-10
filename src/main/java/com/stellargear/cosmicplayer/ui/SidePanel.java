package com.stellargear.cosmicplayer.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SidePanel {

    private final VBox layout = new VBox();
    private final Label home = new Label("Home");
    private final Button chooseFolderBtn = new Button("Elegir carpeta");

    public SidePanel() {
        layout.getChildren().addAll(home, chooseFolderBtn);
    }

    public VBox getNode() {
        return layout;
    }

    public Button getChooseFolderBtn() {
        return chooseFolderBtn;
    }
}
