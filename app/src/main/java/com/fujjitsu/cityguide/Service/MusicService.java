package com.fujjitsu.cityguide.Service;
import com.github.felixgail.gplaymusic.*;
import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.fujjitsu.cityguide.Fragment.Example2Fragment;
import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.test;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import svarzee.gps.gpsoauth.Gpsoauth;

public class MusicService extends IntentService  {
    private static GPlayMusic api;
    private static String USERNAME = "x814850963@gmail.com";
    private static String PASSWORD = "pwls6666";
    private static String ANDROID_ID = "3d422fb1d1dc4a40";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MusicService(String name) {
        super(name);

    }
    public MusicService() {
        super("123");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        api = test.connect();
        switch (intent.getExtras().getString("key"))
        {
            case "1":
                List<Track> tracks = null;
                try {
                    tracks = api.getPromotedTracks();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 发送广播通知Fragment
                Intent sendIntent = new Intent(Example2Fragment.SERVICE_RECEIVER);
                sendIntent.putExtra("tracks", (Serializable) tracks);
                getApplicationContext().sendBroadcast(sendIntent);
        }


    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
