package com.example.fiscalcode_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.fiscalcode_java.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 1, this);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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
                showTermsDialog();
                break;
            case R.id.action_info:
                showInfoDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void showTermsDialog() {
        Dialog infoDialog = new Dialog(this);
        infoDialog.setContentView(R.layout.view_terms);
        TextView textView = infoDialog.findViewById(R.id.terms_text);
        textView.setText(this.getResources().getString(R.string.tcs));
        infoDialog.show();
    }

    public void showInfoDialog() {
        Dialog infoDialog = new Dialog(this);
        infoDialog.setContentView(R.layout.view_info);
        TextView textView = infoDialog.findViewById(R.id.info_text);
        textView.setText(this.getResources().getString(R.string.info));
        infoDialog.show();
    }
}