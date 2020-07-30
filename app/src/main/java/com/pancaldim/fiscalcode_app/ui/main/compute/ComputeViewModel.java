package com.pancaldim.fiscalcode_app.ui.main.compute;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.pancaldim.fiscalcode_app.fiscalcode.utils.ReadTownListHelper;

import java.io.IOException;

import static com.pancaldim.fiscalcode_app.fiscalcode.constants.DateFormatAndLocaleConstants.getLanguage;

public class ComputeViewModel extends ViewModel {

    public static final String TOWNS_FILE = "data/comuni.json";
    public static final String COUNTRIES_FILE = "data/countries_en.json";
    public static final String COUNTRIES_FILE_IT = "data/countries_it.json";

    public String[] getPlaceList(Context context) throws IOException {
        return ReadTownListHelper.readTownNameList(
                context.getAssets().open(TOWNS_FILE),
                context.getAssets().open(getLanguage().equalsIgnoreCase("EN") ? COUNTRIES_FILE : COUNTRIES_FILE_IT)
        );
    }
}
