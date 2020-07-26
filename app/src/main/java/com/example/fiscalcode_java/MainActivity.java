package com.example.fiscalcode_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fiscalcode_java.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setLocale();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 1, this);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setLocale() {
        String currentLang = Locale.getDefault().getLanguage();
        Locale locale = new Locale(currentLang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettingsActivity();
                break;
            case R.id.action_terms:
                showDialogView(R.layout.view_terms);
                break;
            case R.id.action_privacy:
                showDialogView(R.layout.view_privacy);
                break;
            case R.id.action_info:
                showDialogView(R.layout.view_info);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.nothing);
    }

    private void showDialogView(int contentView) {
        Dialog infoDialog = new Dialog(this);
        infoDialog.setContentView(contentView);
        infoDialog.show();
    }
}