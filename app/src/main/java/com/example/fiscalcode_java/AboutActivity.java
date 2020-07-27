package com.example.fiscalcode_java;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fiscalcode_java.fiscalcode.constants.Tables_en;
import com.example.fiscalcode_java.fiscalcode.constants.Tables_it;

import java.util.Locale;

public class AboutActivity extends AppCompatActivity {

    private String LANGUAGE = Locale.getDefault().getLanguage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_about);

        ImageView fcCardImageView = findViewById(R.id.fc_card);
        setImageResource(fcCardImageView, "card");

        ImageView fcPartsImageView = findViewById(R.id.fc_parts);
        setImageResource(fcPartsImageView, "parts");

        WebView dobWebView = findViewById(R.id.table_months);
        dobWebView.loadDataWithBaseURL(null, getDoBTable(), "text/html", "UTF-8", null);

        WebView oddWebView = findViewById(R.id.control_table_odd);
        oddWebView.loadDataWithBaseURL(null, getOddTable(), "text/html", "UTF-8", null);

        WebView evenWebView = findViewById(R.id.control_table_even);
        evenWebView.loadDataWithBaseURL(null, getEvenTable(), "text/html", "UTF-8", null);

        WebView controlWebView = findViewById(R.id.control_table);
        controlWebView.loadDataWithBaseURL(null, getControlTable(), "text/html", "UTF-8", null);

        WebView omoWebView = findViewById(R.id.omo_table);
        omoWebView.loadDataWithBaseURL(null, getOmoTable(), "text/html", "UTF-8", null);
    }

    private void setImageResource(ImageView imageView, String type) {
        switch (type) {
            case "card":
                imageView.setImageResource("EN".equalsIgnoreCase(LANGUAGE) ? R.drawable.fc_card_en : R.drawable.fc_card_it);
                break;
            case "parts":
                imageView.setImageResource("EN".equalsIgnoreCase(LANGUAGE) ? R.drawable.fc_parts_en : R.drawable.fc_parts_it);
        }
    }

    private String getDoBTable() {
        return ("EN".equalsIgnoreCase(LANGUAGE) ? Tables_en.TABLE_DOB : Tables_it.TABLE_DOB);
    }

    private String getOddTable() {
        return ("EN".equalsIgnoreCase(LANGUAGE) ? Tables_en.TABLE_ODD : Tables_it.TABLE_ODD);
    }

    private String getEvenTable() {
        return ("EN".equalsIgnoreCase(LANGUAGE) ? Tables_en.TABLE_EVEN : Tables_it.TABLE_EVEN);
    }

    private String getControlTable() {
        return ("EN".equalsIgnoreCase(LANGUAGE) ? Tables_en.TABLE_CONTROL : Tables_it.TABLE_CONTROL);
    }

    private String getOmoTable() {
        return ("EN".equalsIgnoreCase(LANGUAGE) ? Tables_en.TABLE_OMO : Tables_it.TABLE_OMO);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
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