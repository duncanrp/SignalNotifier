package com.powerdunc.signalnotifier.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    public static String BitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.NO_PADDING);
    }

    public static Bitmap Base64ToBitmap(String base64String) {

        byte[] bytes = Base64.decode(base64String, Base64.NO_PADDING);

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
