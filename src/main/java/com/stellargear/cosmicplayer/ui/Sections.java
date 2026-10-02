package com.stellargear.cosmicplayer.ui;

public enum Sections {
    LIBRARY("Biblioteca"),
    PLAYLISTS("Playlists"),
    SETTINGS("Ajustes");

    private final String label;

    Sections (String label) { this.label = label; }

    public String label() { return label; }
}