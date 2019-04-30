package com.powerdunc.signalnotifier.Providers;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundProvider {

    public static void PlaySound(Context context, int resource)
    {
        MediaPlayer m = MediaPlayer.create(context, resource);

        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });

        m.start();


    }


}
