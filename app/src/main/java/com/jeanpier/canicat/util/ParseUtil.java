package com.jeanpier.canicat.util;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ParseUtil {
    public static String parseBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
