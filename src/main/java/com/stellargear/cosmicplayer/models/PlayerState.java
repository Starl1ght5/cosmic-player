package com.stellargear.cosmicplayer.models;

/**
 * Possible states of the media player.
 */
public enum PlayerState {

    /** Playback is stopped or has not been started yet. */
    STOPPED,

    /** Playback is actively running. */
    PLAYING,

    /** Playback is paused and can be resumed. */
    PAUSED
}