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

/**
 * Manages the user's playlists and keeps them in sync with persistent storage.
 *
 * <p>Every change to the playlist collection is automatically saved through
 * the provided {@link PlaylistStorage}. Consumers can observe the playlist
 * list via {@link #getPlaylists()} to update the UI.</p>
 *
 * @author Starl1ght5
 * @since 1.0
 */
public class PlaylistService {

    private final ObservableList<Playlist> playlists = FXCollections.observableArrayList();

    /**
     * Loads the stored playlists and subscribes to changes so they are
     * persisted automatically on every modification.
     *
     * @param storage the storage used to load and save playlists
     */
    public PlaylistService(PlaylistStorage storage) {
        playlists.setAll(storage.load());
        playlists.addListener((ListChangeListener<Playlist>) c -> storage.save(List.copyOf(playlists)));
    }

    /**
     * Returns an unmodifiable observable list of all playlists.
     * Changes made through this service are reflected in the returned list.
     *
     * @return the observable list of playlists
     */
    public ObservableList<Playlist> getPlaylists() {
        return FXCollections.unmodifiableObservableList(playlists);
    }

    /**
     * Finds a playlist by its unique ID.
     *
     * @param id the ID to search for
     * @return an {@link Optional} containing the playlist, or empty if not found
     */
    public Optional<Playlist> find(String id) {
        return playlists.stream().filter(p -> p.id().equals(id)).findFirst();
    }

    /**
     * Creates a new empty playlist with the given name.
     *
     * @param name the display name for the new playlist
     */
    public void create(String name) { playlists.add(Playlist.create(name)); }

    /**
     * Deletes the playlist with the given ID. Does nothing if it does not exist.
     *
     * @param id the ID of the playlist to delete
     */
    public void delete(String id) { playlists.removeIf(p -> p.id().equals(id)); }

    /**
     * Renames the playlist with the given ID. Does nothing if it does not exist.
     *
     * @param id   the ID of the playlist to rename
     * @param name the new display name
     */
    public void rename(String id, String name) { update(id, p -> p.withName(name)); }

    /**
     * Adds a song file to the playlist with the given ID. Does nothing if the
     * playlist does not exist or already contains the file.
     *
     * @param id   the ID of the playlist
     * @param file the song file to add
     */
    public void addSong(String id, File file) { update(id, p -> p.withFile(file)); }

    /**
     * Removes a song file from the playlist with the given ID. Does nothing if
     * the playlist does not exist or does not contain the file.
     *
     * @param id   the ID of the playlist
     * @param file the song file to remove
     */
    public void removeSong(String id, File file) { update(id, p -> p.withoutFile(file)); }

    private void update(String id, UnaryOperator<Playlist> change) {
        for (int i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).id().equals(id)) {
                playlists.set(i, change.apply(playlists.get(i)));
                return;
            }
        }
    }
}