package com.example.fiscalcode_java.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fiscalcode_java.ui.main.compute.ComputeFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    public SectionsPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
//                fragment = new ComputeFragment(1);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
