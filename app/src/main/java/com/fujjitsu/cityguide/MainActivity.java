package com.fujjitsu.cityguide;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fujjitsu.cityguide.Fragment.Example2Fragment;
import com.fujjitsu.cityguide.Fragment.Example3Fragment;
import com.fujjitsu.cityguide.Fragment.Example4Fragment;
import com.fujjitsu.cityguide.Fragment.Example1Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private Fragment currentFragment=new Fragment();
    private Example1Fragment exampleFragment = new Example1Fragment();
    private Example2Fragment example2Fragment = new Example2Fragment();
    private Example3Fragment example3Fragment = new Example3Fragment();
    private Example4Fragment example4Fragment = new Example4Fragment();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    ImageView button;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(1);
                    return true;
                case R.id.navigation_music:
                    switchFragment(2);
                    return true;
                case R.id.navigation_follow:
                    switchFragment(3);
                    return true;
                case R.id.navigation_me:
                    switchFragment(4);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        currentFragment = exampleFragment;
        manageFragment(savedInstanceState);
        button = findViewById(R.id.titlebar_menu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
    }


    private void manageFragment(Bundle savedInstanceState){
        fragments.add(exampleFragment);
        fragments.add(example2Fragment);
        fragments.add(example3Fragment);
        fragments.add(example4Fragment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(null == savedInstanceState) {
            transaction.add(R.id.f1, exampleFragment);
            transaction.add(R.id.f1, example2Fragment).hide(example2Fragment);
            transaction.add(R.id.f1, example3Fragment).hide(example3Fragment);
            transaction.add(R.id.f1, example4Fragment).hide(example4Fragment);
            transaction.commit();
        }
    }
    //switch fragment
    private void switchFragment(int i){
        switch (i){
            case 1:
                addFragment(exampleFragment).commit();
                break;
            case 2:
                addFragment(example2Fragment).commit();
                break;
            case 3:
                addFragment(example3Fragment).commit();
                break;
            case 4:
                addFragment(example4Fragment).commit();
                break;
        }
    }

    private FragmentTransaction addFragment(Fragment f){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(!f.isAdded())
        {
            if(null != currentFragment)
            {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.f1,f);
        }
        else {
            if(currentFragment==f)
                return transaction;
            transaction.hide(currentFragment).show(f);
        }
        currentFragment = f;
        return transaction;
    }





}
