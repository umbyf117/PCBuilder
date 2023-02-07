package com.example.socialgaming.api;

import android.util.Log;

import androidx.annotation.NonNull;

/*
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

 */

import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.utils.BuildUtils;
import com.example.socialgaming.utils.wrapper.ResponseWrapper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ComponentsFetcher {

    private static final String TAG = "ComponentsFetcher";

    private static final String API_KEY = "10d84ab62bmshe4f51c0ed4a14c1p1fb8cajsnbbeed268e594";
    //35ef5df84fmshcae21a2a6092192p1edff1jsn9ddef0c06104
    //fb275a437bmsh9db28c43aebefc4p1ac617jsnb6bd17a71b69
    //c7dc647869msh660a01d6bceef08p1a9b40jsn9b99c892994a
    //0d99187280mshfaaa256149d07a5p196f2bjsnea45598f64ef
    //10d84ab62bmshe4f51c0ed4a14c1p1fb8cajsnbbeed268e594

    public String fetchItems(ComponentType type, int limit, int offset) {
        final ResponseWrapper responseWrapper = new ResponseWrapper();
        final CountDownLatch latch = new CountDownLatch(1);

        Thread backgroundThread = new Thread(() -> {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://computer-components-api.p.rapidapi.com/" + BuildUtils.getUrl(type) + "?limit=" + limit + "&offset=" + offset)
                    .get()
                    .addHeader("X-RapidAPI-Key", API_KEY)
                    .addHeader("X-RapidAPI-Host", "computer-components-api.p.rapidapi.com")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    latch.countDown();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        responseWrapper.response = responseBody.string();
                    }
                    latch.countDown();
                    response.close();
                }
            });

        });

        backgroundThread.start();

        // Attendere il completamento del thread secondario prima di accedere alla risposta
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return responseWrapper.response;
    }


}
