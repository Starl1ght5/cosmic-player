package com.stellargear.cosmicplayer.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.stellargear.cosmicplayer.models.Song;

/**
 * Manages the list of songs to be played and the playback order.
 *
 * <p>Supports two modes:
 * <ul>
 *   <li>Linear order: songs play in the order given by {@link #setSongs(List)}.</li>
 *   <li>Shuffle order: songs play in a random order, keeping the current
 *       song at the top when the mode is toggled.</li>
 * </ul>
 *
 * <p>Navigation wraps around: {@link #next()} on the last song moves to the
 * first one, and {@link #previous()} on the first song moves to the last one.</p>
 *
 * @author Starl1ght5
 * @since 1.0
 */
public class PlaybackQueue {

    private List<Song> songs = List.of();
    private List<Song> order = List.of();
    private int index = -1;
    private boolean shuffle = false;

    /**
     * Replaces the queue contents. The current song is preserved if it is
     * present in the new list; otherwise the playback position is reset.
     *
     * @param newSongs the songs to play (must not be {@code null})
     */
    public void setSongs(List<Song> newSongs) {
        Song current = current();
        songs = List.copyOf(newSongs);
        rebuild(current);
    }

    /**
     * Enables or disables shuffle mode. When toggled, the queue is
     * rebuilt while keeping the current song selected.
     *
     * @param value {@code true} to enable shuffle, {@code false} for linear order
     */
    public void setShuffle(boolean value) {
        if (shuffle == value) return;
        Song current = current();
        shuffle = value;
        rebuild(current);
    }

    /**
     * Selects the given song as the current one. If the song is not in the
     * queue, the playback position becomes invalid ({@code null} current).
     *
     * @param song the song to select
     */
    public void select(Song song) {
        index = order.indexOf(song);
        current();
    }

    /**
     * Moves to the next song in the queue, wrapping around at the end.
     *
     * @return the new current song, or {@code null} if the queue is empty
     */
    public Song next() {
        return move(1);
    }

    /**
     * Moves to the previous song in the queue, wrapping around at the beginning.
     *
     * @return the new current song, or {@code null} if the queue is empty
     */
    public Song previous() {
        return move(-1);
    }

    /**
     * Returns the song currently selected, or {@code null} if no song is selected
     * or the queue is empty.
     *
     * @return the current song
     */
    public Song current() {
        return index >= 0 && index < order.size() ? order.get(index) : null;
    }

    private Song move(int delta) {
        if (order.isEmpty()) return null;

        if (index < 0) {
            index = delta > 0 ? 0 : order.size() - 1;
        } else {
            index = Math.floorMod(index + delta, order.size());
        }
        return order.get(index);
    }

    private void rebuild(Song current) {
        order = new ArrayList<>(songs);

        if (!shuffle) {
            index = current == null ? -1 : order.indexOf(current);
            return;
        }

        boolean hadCurrent = current != null && order.remove(current);
        Collections.shuffle(order);

        if (hadCurrent) {
            order.addFirst(current);
            index = 0;
        } else {
            index = -1;
        }
    }
}