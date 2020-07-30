package com.pancaldim.fiscalcode.ui.main.extract;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.pancaldim.fiscalcode.fiscalcode.models.Country;
import com.pancaldim.fiscalcode.fiscalcode.models.Town;
import com.pancaldim.fiscalcode.fiscalcode.utils.ReadTownListHelper;

import java.io.IOException;
import java.util.List;

import static com.pancaldim.fiscalcode.fiscalcode.constants.DateFormatAndLocaleConstants.getLanguage;

public class ExtractViewModel extends ViewModel {

    public static final String TOWNS_FILE = "data/comuni.json";
    public static final String COUNTRIES_FILE = "data/countries_en.json";
    public static final String COUNTRIES_FILE_IT = "data/countries_it.json";

    public List<Town> getTownList(Context context) throws IOException {
        return ReadTownListHelper.readTowns(context.getAssets().open(TOWNS_FILE));
    }

    public List<Country> getCountryList(Context context) throws IOException {
        return ReadTownListHelper.readCountries(context.getAssets()
                .open(getLanguage().equalsIgnoreCase("en") ? COUNTRIES_FILE : COUNTRIES_FILE_IT));
    }
}
