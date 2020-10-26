package com.pancaldim.fiscalcode_app;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
                    try {
                        saveFunction(root, fiscalCode);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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

    private void saveFunction(View root, String fiscalCode) throws FileNotFoundException {
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

    private void exportToGallery(String fiscalCode) throws FileNotFoundException {
        RelativeLayout relativeLayout = findViewById(R.id.fc_data_group);
        Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        relativeLayout.draw(canvas);

//        String path = "/storage/emulated/0/Pictures/ComputeFiscalCode";
//        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/ComputeFiscalCode";;
//        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "/ComputeFiscalCode";
//        File outputDir = new File(path);
//        outputDir.mkdirs();
//        File newFile = new File(path + File.separator + fiscalCode + "_test.png");

        if (Build.VERSION.SDK_INT >= 29) {
            ContentValues contentValues = getContentValues();
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "ComputeFiscalCode");
            contentValues.put(MediaStore.Images.Media.IS_PENDING, true);
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                saveImageToStream(bitmap, getContentResolver().openOutputStream(uri));
                contentValues.put(MediaStore.Images.Media.IS_PENDING, false);
                getContentResolver().update(uri, contentValues, null, null);
            }
        } else {
            File outputDir = new File(Environment.getExternalStorageState(Environment.getExternalStorageDirectory()) + "/ComputeFiscalCode");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            File newFile = new File(outputDir + File.separator + fiscalCode + "_test.png");
            saveImageToStream(bitmap, new FileOutputStream(newFile));

            ContentValues contentValues = getContentValues();
            contentValues.put(MediaStore.Images.Media.DATA, newFile.getAbsolutePath());
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        // path to /data/data/yourapp/app_data/imageDir
//        File directory = cw.getDir("ComputeFC", Context.MODE_PRIVATE);
//        // Create imageDir
//        File newFile = new File(directory,fiscalCode + "_test.png");


//        String rootDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
//        File fiscalCodeSaveDir = new File(rootDir + "/ComputeFiscalCode");
//        fiscalCodeSaveDir.mkdirs();
//        String fileName = fiscalCode + ".png";
//        File savedFile = new File(fiscalCodeSaveDir, fileName);
//
//        try {
//            FileOutputStream fos = new FileOutputStream(savedFile);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return contentValues;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream out) {
//        FileOutputStream out;
        //            out = new FileOutputStream(newFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
    }
}
