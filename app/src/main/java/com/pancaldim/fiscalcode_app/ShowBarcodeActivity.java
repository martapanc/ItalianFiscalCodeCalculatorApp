package com.pancaldim.fiscalcode_app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
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

        setupSpeedDial();

//        RelativeLayout relativeLayout = findViewById(R.id.view_show_barcode);
//        Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(), Bitmap.Config.ARGB_8888);
//
//        String root = Arrays.toString(getExternalMediaDirs());
//        File fiscalCodeSaveDir = new File(root + "/saved_fiscal_codes");
//        fiscalCodeSaveDir.mkdirs();
//        String fileName = fiscalCodeData.getFiscalCode() + ".png";
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

    private void setupSpeedDial() {
        SpeedDialView speedDialView = findViewById(R.id.brc_speedDial);
        final int color = getResources().getColor(R.color.colorAccent, null);
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.brc_copy, R.drawable.ic_content_copy_24px)
                .setFabBackgroundColor(color)
                .create());
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.brc_send, R.drawable.ic_share_24px)
                .setFabBackgroundColor(color)
                .create());

        TextView fiscalCode = findViewById(R.id.com_fiscalCodeOutput);

        speedDialView.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()) {
                case R.id.brc_copy:
                    copyFunction(fiscalCode.getText());
                    break;
                case R.id.brc_send:
                    shareFunction(fiscalCode.getText());
                    break;
                default:
                    return false;
            }
            return false;
        });
    }

    private void copyFunction(CharSequence fiscalCode) {
        String message;
//        final Context context = requireContext();
//
//        ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(context).getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData clipData = ClipData.newPlainText("Fiscal code", fiscalCode);
//        Objects.requireNonNull(clipboard).setPrimaryClip(clipData);
//        message = String.format(context.getResources().getString(R.string.copied_to_clipboard), fiscalCode);
//
//        Snackbar.make(root, message, Snackbar.LENGTH_LONG)
//                .setAction("action", null)
//                .show();
    }

    private void shareFunction(CharSequence fiscalCode) {
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, fiscalCode);
//        sendIntent.setType("text/plain");
//
//        Intent shareIntent = Intent.createChooser(sendIntent, null);
//        root.getContext().startActivity(shareIntent);

    }
}
