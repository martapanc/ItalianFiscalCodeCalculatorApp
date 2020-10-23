package com.pancaldim.fiscalcode_app.barcode;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Writer;


public class BarcodeGeneratorUtils {

    private static final int BARCODE_WIDTH = 990;
    private static final int BARCODE_HEIGHT = 190;

    public static Bitmap generateCode39BarcodeImage(String barcodeText) {
        Code39Writer barcodeWriter = new Code39Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_39, BARCODE_WIDTH, BARCODE_HEIGHT);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }
}
