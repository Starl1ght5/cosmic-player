package com.stellargear.cosmicplayer.viewmodels;


import com.stellargear.cosmicplayer.models.Playlist;
import com.stellargear.cosmicplayer.models.Song;
import com.stellargear.cosmicplayer.services.LibraryService;
import com.stellargear.cosmicplayer.services.PlaylistService;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

public class PlaylistViewModel {

    private final PlaylistService playlistService;
    private final LibraryService library;

    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final ObservableList<Song> readOnlySongs = FXCollections.unmodifiableObservableList(songs);
    private final ReadOnlyObjectWrapper<Playlist> selected = new ReadOnlyObjectWrapper<>();
    private String selectedId;

    public PlaylistViewModel(PlaylistService playlistService, LibraryService library) {
        this.playlistService = playlistService;
        this.library = library;

        // Se recalcula si cambia alguna playlist o si termina de cargarse la biblioteca
        playlistService.getPlaylists().addListener((ListChangeListener<Playlist>) c -> refresh());
        library.getSongs().addListener((ListChangeListener<Song>) c -> refresh());
    }

    // ---------- Estado ----------

    public ObservableList<Playlist> getPlaylists() {
        return playlistService.getPlaylists();
    }

    /// Canciones de la playlist seleccionada
    public ObservableList<Song> getSongs() {
        return readOnlySongs;
    }

    public ReadOnlyObjectProperty<Playlist> selectedProperty() {
        return selected.getReadOnlyProperty();
    }

    // ---------- Comandos ----------

    public void select(Playlist playlist) {
        selectedId = playlist == null ? null : playlist.id();
        refresh();
    }

    public void create(String name)                 { playlistService.create(name); }
    public void rename(Playlist p, String name)     { playlistService.rename(p.id(), name); }
    public void delete(Playlist p)                  { playlistService.delete(p.id()); }

    public void addSong(Playlist p, Song song) {
        if (song != null) playlistService.addSong(p.id(), song.file());
    }

    public void removeFromSelected(Song song) {
        if (song != null && selectedId != null) {
            playlistService.removeSong(selectedId, song.file());
        }
    }

    // ---------- Internos ----------

    private void refresh() {
        Optional<Playlist> playlist = selectedId == null
                ? Optional.empty()
                : playlistService.find(selectedId);

        if (playlist.isEmpty()) selectedId = null;       // la borraron

        selected.set(playlist.orElse(null));
        songs.setAll(playlist.map(this::resolve).orElse(List.of()));
    }

    /// Convierte las rutas guardadas en canciones; las que ya no existen se omiten
    private List<Song> resolve(Playlist playlist) {
        return playlist.files().stream()
                .map(library::find)
                .flatMap(Optional::stream)
                .toList();
    }
}