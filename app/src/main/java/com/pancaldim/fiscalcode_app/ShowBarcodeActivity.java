package com.pancaldim.fiscalcode_app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pancaldim.fiscalcode_app.barcode.BarcodeGeneratorUtils;
import com.pancaldim.fiscalcode_app.fiscalcode.models.FiscalCodeData;

public class ShowBarcodeActivity extends AppCompatActivity {

    public static final String FISCAL_CODE_DATA_EXTRA_KEY = "fiscalCodeData";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_show_barcode);

        FiscalCodeData fiscalCodeData = (FiscalCodeData) getIntent().getSerializableExtra(FISCAL_CODE_DATA_EXTRA_KEY);

        if (fiscalCodeData != null) {
            TextView firstNameTextView = findViewById(R.id.brc_first_name_text);
            firstNameTextView.setText(fiscalCodeData.getFirstNameCode());

            TextView lastNameTextView = findViewById(R.id.brc_last_name_text);
            lastNameTextView.setText(fiscalCodeData.getLastNameCode());

            TextView genderTextView = findViewById(R.id.brc_gender_text);
            genderTextView.setText(fiscalCodeData.getGender().toString());

            TextView dobTextView = findViewById(R.id.brc_dob_text);
            dobTextView.setText(fiscalCodeData.getDateOfBirth());

            TextView pobTextView = findViewById(R.id.brc_pob_text);
            pobTextView.setText(fiscalCodeData.getPlaceOfBirth());

            TextView fiscalCodeTextView = findViewById(R.id.brc_fc_text);
            fiscalCodeTextView.setText(fiscalCodeData.getFiscalCode());

            ImageView barcodeImageView = findViewById(R.id.brc_barcodeOutput);
            barcodeImageView.setImageBitmap(BarcodeGeneratorUtils.generateCode39BarcodeImage(fiscalCodeData.getFiscalCode()));
        }
    }
}
