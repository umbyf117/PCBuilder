package com.example.socialgaming.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageUtils {

    public static byte[] encodeBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap decodeByteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static List<Integer> encodeArrayToList(byte[] byteArray) {
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < byteArray.length; i++)
            result.add((int) byteArray[i]);
        return result;
    }

    public static byte[] decodeListToArray(List<Long> byteList) {
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i).byteValue();
        }
        return byteArray;
    }

}
