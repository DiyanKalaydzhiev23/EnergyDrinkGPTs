package com.example.enrgydrinksgpts;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaPlayerHandler {

    private MediaPlayer mediaPlayer;

    public MediaPlayerHandler(Context context, int soundResourceId) {
        mediaPlayer = MediaPlayer.create(context, soundResourceId);
    }

    public void play() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
