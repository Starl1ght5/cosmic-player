package com.stellargear.cosmicplayer.services;

import com.stellargear.cosmicplayer.models.Playlist;
import com.stellargear.cosmicplayer.services.persistence.PlaylistStorage;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class PlaylistService {

    private final ObservableList<Playlist> playlists = FXCollections.observableArrayList();

    public PlaylistService(PlaylistStorage storage) {
        playlists.setAll(storage.load());
        playlists.addListener((ListChangeListener<Playlist>) c -> storage.save(List.copyOf(playlists)));
    }

    public ObservableList<Playlist> getPlaylists() {
        return FXCollections.unmodifiableObservableList(playlists);
    }

    public Optional<Playlist> find(String id) {
        return playlists.stream().filter(p -> p.id().equals(id)).findFirst();
    }

    public void create(String name)                { playlists.add(Playlist.create(name)); }
    public void delete(String id)                  { playlists.removeIf(p -> p.id().equals(id)); }
    public void rename(String id, String name)     { update(id, p -> p.withName(name)); }
    public void addSong(String id, File file)      { update(id, p -> p.withFile(file)); }
    public void removeSong(String id, File file)   { update(id, p -> p.withoutFile(file)); }

    private void update(String id, UnaryOperator<Playlist> change) {
        for (int i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).id().equals(id)) {
                playlists.set(i, change.apply(playlists.get(i)));
                return;
            }
        }
    }
}