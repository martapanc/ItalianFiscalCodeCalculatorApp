package com.pancaldim.fiscalcode_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.pancaldim.fiscalcode_app.barcode.BarcodeGeneratorUtils;
import com.pancaldim.fiscalcode_app.fiscalcode.models.FiscalCodeData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShowBarcodeActivity extends AppCompatActivity {

    public static final String FISCAL_CODE_DATA_EXTRA_KEY = "fiscalCodeData";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_show_barcode);

        FiscalCodeData fiscalCodeData = (FiscalCodeData) getIntent().getSerializableExtra(FISCAL_CODE_DATA_EXTRA_KEY);

        if (fiscalCodeData != null) {
            final String fiscalCode = fiscalCodeData.getFiscalCode();

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
            fiscalCodeTextView.setText(fiscalCode);

            ImageView barcodeImageView = findViewById(R.id.brc_barcodeOutput);
            barcodeImageView.setImageBitmap(BarcodeGeneratorUtils.generateCode39BarcodeImage(fiscalCode));

            setupSpeedDial(fiscalCode);
        }
    }

    private void setupSpeedDial(String fiscalCode) {
        View root = findViewById(android.R.id.content).getRootView();
        SpeedDialView speedDialView = findViewById(R.id.brc_speedDial);
        final int color = getResources().getColor(R.color.colorAccent, null);
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.brc_save, R.drawable.ic_save_alt_24px)
                .setFabBackgroundColor(color)
                .create());
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.brc_share, R.drawable.ic_share_24px)
                .setFabBackgroundColor(color)
                .create());

        speedDialView.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()) {
                case R.id.brc_save:
                    saveFunction(root, fiscalCode);
                    break;
                case R.id.brc_share:
                    shareFunction(root, fiscalCode);
                    break;
                default:
                    return false;
            }
            return false;
        });
    }

    private void saveFunction(View root, String fiscalCode) {
        exportToGallery(fiscalCode);
        Snackbar.make(root, "Saved to gallery", Snackbar.LENGTH_LONG)
                .setAction("action", null)
                .show();
    }

    private void shareFunction(View root, CharSequence fiscalCode) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, fiscalCode);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        root.getContext().startActivity(shareIntent);
    }

    private void exportToGallery(String fiscalCode) {
        RelativeLayout relativeLayout = findViewById(R.id.fc_data_group);
        Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(), Bitmap.Config.ARGB_8888);

        String rootDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File fiscalCodeSaveDir = new File(rootDir + "/ComputeFiscalCode");
        fiscalCodeSaveDir.mkdirs();
        String fileName = fiscalCode + ".png";
        File savedFile = new File(fiscalCodeSaveDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(savedFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
