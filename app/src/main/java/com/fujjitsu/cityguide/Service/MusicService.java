package com.fujjitsu.cityguide.Service;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;

public class MusicService  extends IntentService implements MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener{
    private static final String ACTION_PLAY = "com.example.action.PLAY";
    private static final String ACTION_STOP = "com.example.action.STOP";
    private static final String ACTION_START= "com.example.action.START";
    MediaPlayer mMediaPlayer = null;
    private static final String TAG = "MyActivity";
    public MusicService(){
        super("lalala");
    }
    public MusicService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();
        String url = "http://www.brothershouse.narod.ru/music/pepe_link_-_guitar_vibe_113_club_mix.mp3"; // your URL here
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare(); // prepare async to not block main thread
        } catch (IOException e) {
            Toast.makeText(this, "mp3 not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //mp3 will be started after completion of preparing...
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                 @Override
                 public void onPrepared(MediaPlayer mp) {
                     mp.start();
                 }
             });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra("key").equals(ACTION_STOP)) {
            mMediaPlayer.pause();
        }
        else if (intent.getStringExtra("key").equals(ACTION_START)) {
            mMediaPlayer.start();
        }
        else
        {
            mMediaPlayer.release();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    public void initMediaPlayer() {
        // ...initialize the MediaPlayer here...
        mMediaPlayer =  new MediaPlayer(); // initialize it
        mMediaPlayer.reset();
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "OnError - Error code: " + what + " Extra code: " + extra);
        switch (what) {
            case -1004:
                Log.d(TAG, "MEDIA_ERROR_IO");
                break;
            case -1007:
                Log.d(TAG, "MEDIA_ERROR_MALFORMED");
                break;
            case 200:
                Log.d(TAG, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                break;
            case 100:
                Log.d(TAG, "MEDIA_ERROR_SERVER_DIED");
                break;
            case -110:
                Log.d(TAG, "MEDIA_ERROR_TIMED_OUT");
                break;
            case 1:
                Log.d(TAG, "MEDIA_ERROR_UNKNOWN");
                break;
            case -1010:
                Log.d(TAG, "MEDIA_ERROR_UNSUPPORTED");
                break;
        }
        switch (extra) {
            case 800:
                Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING");
                break;
            case 702:
                Log.d(TAG, "MEDIA_INFO_BUFFERING_END");
                break;
            case 701:
                Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE");
                break;
            case 802:
                Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE");
                break;
            case 801:
                Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE");
                break;
            case 1:
                Log.d(TAG, "MEDIA_INFO_UNKNOWN");
                break;
            case 3:
                Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START");
                break;
            case 700:
                Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING");
                break;
        }
        Toast.makeText(getApplicationContext(),"播放错误!!",Toast.LENGTH_LONG).show();
        return true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer != null)
            mMediaPlayer.release();
    }
}
