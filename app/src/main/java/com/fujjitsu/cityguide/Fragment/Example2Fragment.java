package com.fujjitsu.cityguide.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fujjitsu.cityguide.Adapter.LinearAdapter;
import com.fujjitsu.cityguide.R;
import com.fujjitsu.cityguide.Service.MusicService;

import java.util.ArrayList;

public class Example2Fragment extends Fragment {
    private View view;//定义view用来设置fragment的layout
    public RecyclerView recyclerView;//定义RecyclerView
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<String> goodsEntityList = new ArrayList<String>();
    //自定义recyclerveiw的适配器
    private LinearAdapter linearAdapter;

    private Button btn_start;
    private Button btn_stop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.example2_fragment,container,false);
        initView();
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getActivity(), MusicService.class);
                mIntent.putExtra("key","com.example.action.START");
                getActivity().startService(mIntent);
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getActivity(), MusicService.class);
                mIntent.putExtra("key","com.example.action.STOP");
                getActivity().startService(mIntent);
            }
        });

        return view;
    }

    private void initView() {
        btn_start =  view.findViewById(R.id.btn_start);
        btn_stop=  view.findViewById(R.id.btn_stop);
    }


}
