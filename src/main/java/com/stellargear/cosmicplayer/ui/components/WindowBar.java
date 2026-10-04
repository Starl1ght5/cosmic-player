package com.stellargear.cosmicplayer.ui.components;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class WindowBar {

    private HBox titleBar = new HBox();

    private double dragX, dragY;

    public WindowBar (Stage stage) {
        Label title = new Label("Cosmic Music Player");
        title.getStyleClass().add("app-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button min   = windowButton("mdi2w-window-minimize", false);
        Button max   = windowButton("mdi2w-window-maximize", false);
        Button close = windowButton("mdi2c-close", true);

        min.setOnAction(e -> stage.setIconified(true));
        max.setOnAction(e -> stage.setMaximized(!stage.isMaximized()));
        close.setOnAction(e -> stage.close());

        titleBar = new HBox(title, spacer, min, max, close);
        titleBar.getStyleClass().add("title-bar");

        titleBar.setOnMousePressed(e -> { dragX = e.getSceneX(); dragY = e.getSceneY(); });
        titleBar.setOnMouseDragged(e -> {
            if (!stage.isMaximized()) {
                stage.setX(e.getScreenX() - dragX);
                stage.setY(e.getScreenY() - dragY);
            }
        });
        titleBar.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) stage.setMaximized(!stage.isMaximized());
        });
    }

    private Button windowButton(String icon, boolean isClose) {
        Button b = new Button();
        b.setGraphic(new FontIcon(icon));
        b.getStyleClass().add("window-btn");
        if (isClose) b.getStyleClass().add("close");
        return b;
    }

    public HBox getNode() {
        return titleBar;
    }
}
