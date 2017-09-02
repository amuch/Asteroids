package com.example.adam.asteroids;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Display;

import java.io.IOException;

public class GameActivity extends Activity {
    GameView gameView;
    Display display;
    Point size;

    private SoundPool soundPool;
    static int laserBlast = -1;
    static int smash = -1;
    static int whack = -1;
    static int jazzyError = -1;
    static int error = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        /********************************
        // Default constructor.
        gameView = new GameView(this);
        *********************************/

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor assetFileDescriptor;

            assetFileDescriptor = assetManager.openFd("laserBlast.ogg");
            laserBlast = soundPool.load(assetFileDescriptor, 0);

            assetFileDescriptor = assetManager.openFd("smash.ogg");
            smash = soundPool.load(assetFileDescriptor, 0);

            assetFileDescriptor = assetManager.openFd("whack.ogg");
            whack = soundPool.load(assetFileDescriptor, 0);

            assetFileDescriptor = assetManager.openFd("jazzyError.ogg");
            jazzyError = soundPool.load(assetFileDescriptor, 0);

            assetFileDescriptor = assetManager.openFd("error.ogg");
            error = soundPool.load(assetFileDescriptor, 0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //Game specific constructor.
        gameView = new GameView(this, size, soundPool);

         setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }
}
