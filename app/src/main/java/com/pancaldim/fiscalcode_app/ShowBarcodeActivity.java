package com.pancaldim.fiscalcode_app;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.pancaldim.fiscalcode_app.barcode.BarcodeGeneratorUtils;
import com.pancaldim.fiscalcode_app.fiscalcode.models.FiscalCodeData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
                        saveFunction();
                    } catch (IOException e) {
                        showToastWithMessage(R.string.save_error);
                        e.printStackTrace();
                    }
                    break;
                case R.id.brc_share:
                    try {
                        Uri uri = saveImageExternal(getBitmapFromLayout(), fiscalCode);
                        shareFunction(uri);
                    } catch (IOException e) {
                        showToastWithMessage(R.string.share_error);
                        e.printStackTrace();
                    }
                    break;
                default:
                    return false;
            }
            return false;
        });
    }

    private void saveFunction() throws IOException {
        Bitmap bitmap = getBitmapFromLayout();
        final String saveDirectory = getMessage(R.string.save_directory);

        if (Build.VERSION.SDK_INT >= 29) {
            ContentValues contentValues = getContentValues();
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + saveDirectory);
            contentValues.put(MediaStore.Images.Media.IS_PENDING, true);
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                saveImageToStream(bitmap, getContentResolver().openOutputStream(uri));
                contentValues.put(MediaStore.Images.Media.IS_PENDING, false);
                getContentResolver().update(uri, contentValues, null, null);
                showToastWithMessage(R.string.saved_to_gallery);
            }
        } else {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(ShowBarcodeActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                saveToGalleryAPI28();
            }
        }
    }

    private void shareFunction(Uri uri) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setType("image/png");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveToGalleryAPI28();
            } else {
                showToastWithMessage(R.string.save_permission_error);
            }
        }
    }

    public void saveToGalleryAPI28() {
        File outputDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                File.separator + getMessage(R.string.save_directory));
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File newFile = new File(outputDir, System.currentTimeMillis() + ".png");
        FileOutputStream out;
        try {
            out = new FileOutputStream(newFile);
            saveImageToStream(getBitmapFromLayout(), out);
            ContentValues contentValues = getContentValues();
            contentValues.put(MediaStore.Images.Media.DATA, newFile.getAbsolutePath());
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            showToastWithMessage(R.string.saved_to_gallery);
        } catch (IOException e) {
            showToastWithMessage(R.string.save_error);
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromLayout() {
        RelativeLayout relativeLayout = findViewById(R.id.fc_data_barcode_group);
        Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        relativeLayout.draw(canvas);
        return bitmap;
    }

    private ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return contentValues;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream out) throws IOException {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.flush();
        out.close();
    }

    /**
     * Saves the image as PNG to the app's private external storage folder.
     *
     * @param bitmap     Bitmap to save.
     * @param fiscalCode Computer Fiscal Code to be used as file name.
     * @return Uri of the saved file or null
     */
    private Uri saveImageExternal(Bitmap bitmap, String fiscalCode) throws IOException {
        Uri uri;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fiscalCode + ".png");
            saveImageToStream(bitmap, new FileOutputStream(file));
            final Context context = getBaseContext();
            uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        } catch (IOException e) {
            throw new IOException();
        }
        return uri;
    }

    private String getMessage(int stringId) {
        return getResources().getString(stringId);
    }

    public void showToastWithMessage(int stringId) {
        Toast.makeText(ShowBarcodeActivity.this, getMessage(stringId), Toast.LENGTH_LONG).show();
    }
}
