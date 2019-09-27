package com.fujjitsu.cityguide.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fujjitsu.cityguide.R;

import java.io.IOException;

public class Example3Fragment extends Fragment {
    private View view;//定义view用来设置fragment的layout
    private Button btn_start;
    private Button btn_stop;
    private MediaPlayer mMediaPlayer;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMediaPlayer();
        String url = "https://r1---sn-ogueln7z.c.doc-0-0-sj.sj.googleusercontent.com/videoplayback?id=d3cec2a4b9205370&itag=25&source=skyjam&begin=0&ei=eXOMXZfhJc3WgAOZnZ2YBw&o=12233349780008635623&cmbypass=yes&ratebypass=yes&cpn=R1i9VIneTzk2MRovnhSZ1A&ip=0.0.0.0&ipbits=0&expire=1569485779&sparams=cmbypass,ei,expire,id,initcwndbps,ip,ipbits,itag,mip,mm,mn,ms,mv,mvi,o,pl,ratebypass,source&signature=6FC0D9147005FDABF8A0820273B869F6E111BE99.03B1DBD270C37D95E6DEC0F54FA451FFC9C77C95&key=cms1&initcwndbps=7930&mip=126.228.142.200&mm=31&mn=sn-ogueln7z&ms=au&mt=1569485501&mv=m&mvi=0&pl=16"; // your URL here
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        } catch (IOException e) {
            Toast.makeText(getActivity(), "mp3 not found", Toast.LENGTH_SHORT).show();
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
    public void initMediaPlayer() {
        // ...initialize the MediaPlayer here...
        mMediaPlayer =  new MediaPlayer(); // initialize it
        mMediaPlayer.reset();
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(getContext(),"123",Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }


    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(getActivity(),"播放错误!!",Toast.LENGTH_LONG).show();
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer != null)
            mMediaPlayer.release();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.example3_fragment,container,false);
        initView();
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.start();
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.pause();
            }
        });
        return view;
    }

    private void initView() {
        btn_start =  view.findViewById(R.id.button111);
        btn_stop=  view.findViewById(R.id.button222);
    }
}
