package com.pancaldim.fiscalcode_app.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pancaldim.fiscalcode_app.R;
import com.pancaldim.fiscalcode_app.ui.main.compute.ComputeFragment;
import com.pancaldim.fiscalcode_app.ui.main.extract.ExtractFragment;
import com.pancaldim.fiscalcode_app.ui.main.verify.VerifyFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    private final int[] TAB_TITLES = new int[]{
            R.string.tab_compute,
            R.string.tab_extract,
            R.string.tab_verify
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
                fragment = new ExtractFragment();
                break;
            case 2:
                fragment = new VerifyFragment();
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
