package com.stellargear.cosmicplayer.ui.components;

import com.stellargear.cosmicplayer.ui.Sections;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;


public class SidePanel {

    private final VBox layout = new VBox();
    private final ObjectProperty<Sections> selected = new SimpleObjectProperty<>(Sections.LIBRARY);

    public SidePanel() {
        ToggleGroup group = new ToggleGroup();

        for (Sections section : Sections.values()) {
            ToggleButton button = new ToggleButton(section.label());
            button.setToggleGroup(group);
            button.setSelected(section == selected.get());
            button.setOnAction(e -> selected.set(section));
            button.getStyleClass().add("sidepanel-button");
            layout.getChildren().add(button);
        }

        group.selectedToggleProperty().addListener((obs, old, now) -> {
            if (now == null && old != null) old.setSelected(true);
        });

        layout.getStyleClass().add("sidepanel");
    }

    public ObjectProperty<Sections> selectedSectionProperty() { return selected; }

    public VBox getNode() { return layout; }
}