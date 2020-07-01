package com.example.fiscalcode_java.ui.main.compute;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.fiscalcode_java.fiscalcode.utils.ReadTownListHelper;

import java.io.IOException;

public class ComputeViewModel extends ViewModel {

    public static final String TOWNS_FILE = "data/comuni.json";
    public static final String COUNTRIES_FILE = "data/countries_it.json";

    public String[] getPlaceList(Context context) throws IOException {
        return ReadTownListHelper.readTownNameList(
                context.getAssets().open(TOWNS_FILE),
                context.getAssets().open(COUNTRIES_FILE)
        );
    }
}
