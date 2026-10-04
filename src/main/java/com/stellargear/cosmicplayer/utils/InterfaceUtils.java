package com.stellargear.cosmicplayer.utils;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Slider;

import java.util.Locale;

public final class InterfaceUtils {

    private InterfaceUtils () {}

    public static void bindSliderFill(Slider slider) {
        Runnable update = () -> {
            Node track = slider.lookup(".track");
            if (track == null) return;
            double range = slider.getMax() - slider.getMin();
            double pct = range == 0 ? 0 : (slider.getValue() - slider.getMin()) / range * 100;
            track.setStyle(String.format(Locale.US,
                    "-fx-background-color: linear-gradient(to right, " +
                            "#7C5CFF 0%%, #22D3EE %.1f%%, #2A2A4A %.1f%%, #2A2A4A 100%%);", pct, pct));
        };
        slider.valueProperty().addListener((o, a, b) -> update.run());
        slider.widthProperty().addListener((o, a, b) -> update.run());
        slider.skinProperty().addListener((o, a, b) -> Platform.runLater(update));
        Platform.runLater(update);
    }
}
