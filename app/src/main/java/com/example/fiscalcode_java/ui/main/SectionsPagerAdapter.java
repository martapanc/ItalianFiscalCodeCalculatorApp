package com.example.fiscalcode_java.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fiscalcode_java.R;
import com.example.fiscalcode_java.ui.main.compute.ComputeFragment;
import com.example.fiscalcode_java.ui.main.extract.ExtractFragment;
import com.example.fiscalcode_java.ui.main.verify.VerifyFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    private int[] TAB_TITLES = new int[]{
            R.string.tab_compute,
            R.string.tab_verify,
            R.string.tab_extract
    };

    public SectionsPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new ComputeFragment();
                break;
            case 1:
                fragment = new VerifyFragment();
                break;
            case 2:
                fragment = new ExtractFragment();
                break;
            default:
                fragment = new Fragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
