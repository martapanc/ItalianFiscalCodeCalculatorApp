package com.example.fiscalcode_java;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference feedbackPreference = findPreference("feedback");
            feedbackPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Dialog feedbackDialog = new Dialog(getContext());
                    InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.WHITE), 25);
                    feedbackDialog.getWindow().setBackgroundDrawable(inset);
                    feedbackDialog.setContentView(R.layout.view_feedback);
                    feedbackDialog.show();
                    return true;
                }
            });
//
//            Locale currentLang = getContext().getResources().getConfiguration().getLocales().get(0);
//            ListPreference listPreference = findPreference("language");
//            listPreference.setDefaultValue(currentLang.toString());
//
//            listPreference.setOnPreferenceChangeListener((preference, newLang) -> {
//                Locale locale = new Locale(newLang.toString());
//                Configuration configuration = new Configuration(getContext().getResources().getConfiguration());
//                Locale.setDefault(locale);
//                configuration.setLocale(locale);
//                return true;
//            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
            overridePendingTransition(R.anim.nothing, R.anim.exit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}