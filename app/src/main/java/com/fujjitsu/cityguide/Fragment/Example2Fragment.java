package com.fujjitsu.cityguide.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fujjitsu.cityguide.Adapter.LinearAdapter;
import com.fujjitsu.cityguide.R;

import java.util.ArrayList;

public class Example2Fragment extends Fragment {
    private View view;//定义view用来设置fragment的layout
    public RecyclerView recyclerView;//定义RecyclerView
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<String> goodsEntityList = new ArrayList<String>();
    //自定义recyclerveiw的适配器
    private LinearAdapter linearAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.example2_fragment,container,false);
        //模拟数据
        initData();
        //对recycleview进行配置
        initRecyclerView();
        return view;
    }

    private void initData() {
        for (int i=0;i<10;i++){
            goodsEntityList.add("lalala"+i);
        }
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle);
        linearAdapter = new LinearAdapter(getActivity(),goodsEntityList);
        recyclerView.setAdapter(linearAdapter);
    }


}
