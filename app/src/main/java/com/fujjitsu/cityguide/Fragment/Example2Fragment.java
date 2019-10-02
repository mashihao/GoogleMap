package com.fujjitsu.cityguide.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fujjitsu.cityguide.Adapter.MusicPagerAdapter;
import com.fujjitsu.cityguide.R;
import com.fujjitsu.cityguide.Service.MusicService;
import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.google.android.material.tabs.TabLayout;

import svarzee.gps.gpsoauth.AuthToken;

public class Example2Fragment extends Fragment {

    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    MusicPagerAdapter musicPagerAdapter;
    ViewPager viewPager;
    View view;

    private Intent intent;
    private MsgReceiver msgReceiver;
    public static String SERVICE_RECEIVER = "com.fujjitsu.cityguide";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 动态注册广播接收器
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SERVICE_RECEIVER);
        getActivity().registerReceiver(msgReceiver, intentFilter);
        //启动service
        Intent intent=new Intent(getContext(), MusicService.class);
        intent.putExtra("key","1");
        getActivity().startService(intent);
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
     view = inflater.inflate(R.layout.example2_fragment, container, false);
     return view;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        musicPagerAdapter = new MusicPagerAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.music_pager);
        viewPager.setAdapter(musicPagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*处理接收到的广播内容*/
            if(null != intent.getExtras().getSerializable("tracks"))
            {
                System.out.println(intent.getExtras().getSerializable("tracks"));
            }
        }
    }

    @Override
    public void onDestroy() {

        // 停止服务
        getActivity().stopService(intent);

        // 注销广播
        getActivity().unregisterReceiver(msgReceiver);

        super.onDestroy();
    }
}



