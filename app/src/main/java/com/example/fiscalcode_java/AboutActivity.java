package com.example.fiscalcode_java;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fiscalcode_java.fiscalcode.constants.Tables_en;
import com.example.fiscalcode_java.fiscalcode.constants.Tables_it;

import java.util.Locale;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_about);

        WebView webView = findViewById(R.id.table_months);
        webView.loadDataWithBaseURL(null, getDoBTable(),
                "text/html", "UTF-8", null);

    }

    private String getDoBTable() {
        String language = Locale.getDefault().getLanguage();
        if ("EN".equalsIgnoreCase(language)) {
            return Tables_en.TABLE_DOB;
        } else {
            return Tables_it.TABLE_DOB;
        }
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