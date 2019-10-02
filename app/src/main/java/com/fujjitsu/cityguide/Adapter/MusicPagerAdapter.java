package com.fujjitsu.cityguide.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fujjitsu.cityguide.Fragment.Example5Fragment;
import com.fujjitsu.cityguide.Fragment.PlayListFragment;

public class MusicPagerAdapter extends FragmentStatePagerAdapter {


    public MusicPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return new PlayListFragment();
            case 1:
                return new Example5Fragment();
            case 2:
                return new Example5Fragment();
            case 3:
                return new Example5Fragment();
        }
        return new Example5Fragment();

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "PLAYLISTS ";
            case 1:
                return "ARTISTS";
            case 2:
                return "ALBUMS";
            case 3:
                return "SONGS";
        }
        return null;
    }
}
