package com.example.socialgaming.api;

import android.util.Log;

import androidx.annotation.NonNull;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ComponentsFetchr {

    private static final String TAG = "ComponentsFetchr";

    private static final String API_KEY = "fb275a437bmsh9db28c43aebefc4p1ac617jsnb6bd17a71b69";

    /*public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getURLString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }*/

    public void fetchItems() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://computer-components-api.p.rapidapi.com/power_supply?limit=5&offset=0")
                .get()
                .addHeader("X-RapidAPI-Key", "fb275a437bmsh9db28c43aebefc4p1ac617jsnb6bd17a71b69")
                .addHeader("X-RapidAPI-Host", "computer-components-api.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.e(TAG, body);
                }
            }
        });
    }

    // UTILIZZA MOCHI PER FARE UN PARSE JSON DELLA REQUEST DI OKHTTPS MA NON FUNGE PORCODIO
    /*public void parseItems() {
        OkHttpClient client = new OkHttpClient();
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Gist> gistJsonAdapter = moshi.adapter(Gist.class);

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("https://api.github.com/gists/c2a7c39532239ff261be")
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Gist gist = gistJsonAdapter.fromJson(response.body().source());

                for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
                    System.out.println(entry.getKey());
                    System.out.println(entry.getValue().content);
                }
            }
        }

        static class Gist {
            Map<String, GistFile> files;
        }

        static class GistFile {
            String content;
        }*/

}
