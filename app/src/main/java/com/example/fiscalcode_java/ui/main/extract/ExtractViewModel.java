package com.example.fiscalcode_java.ui.main.extract;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.fiscalcode_java.fiscalcode.models.Country;
import com.example.fiscalcode_java.fiscalcode.models.Town;
import com.example.fiscalcode_java.fiscalcode.utils.ReadTownListHelper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ExtractViewModel extends ViewModel {

    public static final String TOWNS_FILE = "data/comuni.json";
    public static final String COUNTRIES_FILE = "data/countries_en.json";
    public static final String COUNTRIES_FILE_IT = "data/countries_it.json";

    private final String LANGUAGE = Locale.getDefault().getLanguage();

    public List<Town> getTownList(Context context) throws IOException {
        return ReadTownListHelper.readTowns(context.getAssets().open(TOWNS_FILE));
    }

    public List<Country> getCountryList(Context context) throws IOException {
        return ReadTownListHelper.readCountries(context.getAssets()
                .open(LANGUAGE.equalsIgnoreCase("en") ? COUNTRIES_FILE : COUNTRIES_FILE_IT));
    }
}
