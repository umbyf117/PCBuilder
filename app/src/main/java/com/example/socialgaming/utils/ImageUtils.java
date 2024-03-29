package com.example.socialgaming.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.socialgaming.utils.wrapper.ImageWrapper;
import com.example.socialgaming.utils.wrapper.ResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ImageUtils {

    private static final int MAX_DIMENSION = 512;

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

    public static Bitmap getBitmapFromURL(String src) {
        final ImageWrapper imageWrapper = new ImageWrapper();
        final CountDownLatch latch = new CountDownLatch(1);

        Thread backgroundThread = new Thread(() -> {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                imageWrapper.image = BitmapFactory.decodeStream(input);
                latch.countDown();
            } catch (IOException e) {
                latch.countDown();
            }
        });

        backgroundThread.start();

        // Attendere il completamento del thread secondario prima di accedere alla risposta
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return imageWrapper.image;
    }

    public static Bitmap resize(Bitmap bitmap) {
        double width = bitmap.getWidth();
        double height = bitmap.getHeight();

        if(width < height) {
            height = height * (MAX_DIMENSION / width);
            width = MAX_DIMENSION;
        }
        else {
            width = width * (MAX_DIMENSION / height);
            height = MAX_DIMENSION;
        }

        return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, false);
    }

}
