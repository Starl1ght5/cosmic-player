package com.stellargear.cosmicplayer.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.stellargear.cosmicplayer.models.Song;

public class PlaybackQueue {

    private List<Song> songs = List.of();
    private List<Song> order = List.of();
    private int index = -1;
    private boolean shuffle = false;

    public void setSongs(List<Song> newSongs) {
        Song current = current();
        songs = List.copyOf(newSongs);
        rebuild(current);
    }

    public void setShuffle(boolean value) {
        if (shuffle == value) return;
        Song current = current();
        shuffle = value;
        rebuild(current);
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public Song select(Song song) {
        index = order.indexOf(song);
        return current();
    }

    public Song next() {
        return move(1);
    }

    public Song previous() {
        return move(-1);
    }

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