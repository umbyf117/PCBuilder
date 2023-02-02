package com.example.socialgaming.utils;

import android.util.Log;

import com.example.socialgaming.data.*;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.utils.wrapper.ResponseWrapper;
import com.example.socialgaming.view.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BuildUtils {

    private static final String URL = "https://computer-components-api.p.rapidapi.com/%COMPONENT_TYPE%?limit=%LIMIT%&offset=%OFFSET%";

    public static String getComponentsJSON(ComponentType type, int limit, int offset) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        String url = getUrl(type, limit, offset);

        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("X-RapidAPI-Key", "35ef5df84fmshcae21a2a6092192p1edff1jsn9ddef0c06104")
                        .addHeader("X-RapidAPI-Host", "computer-components-api.p.rapidapi.com")
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    responseWrapper.response = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        backgroundThread.start();

        // Attendere il completamento del thread secondario prima di accedere alla risposta
        try {
            backgroundThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

      return responseWrapper.response;
    }

    public static ComponentBase[] getComponents(String json, ComponentType type) {

        if (json != null) {
            try {
                JSONArray obs = new JSONArray(json);
                ComponentBase[] components = new ComponentBase[obs.length()];
                for(int i = 0; i < obs.length(); i++) {
                    JSONObject obj = obs.getJSONObject(i);
                    ComponentBase component = getComponent(obj, type);
                }

            } catch (final JSONException e) {
                Log.e(HomeActivity.class.getSimpleName(), "Json parsing error: " + e.getMessage());
            }
        }

      return null;
    }

    public static ComponentBase getComponent(JSONObject obj, ComponentType type) {
        ComponentBase component = null;

        component = ComponentBase.construct(type);

        try {
            component.setJSONData(obj);
        } catch (JSONException e) {
            Log.e(HomeActivity.class.getSimpleName(), "Json getting data error: " + e.getMessage());
        }

        return component;
    }

    public static String getUrl(ComponentType type, int limit, int offset) {
        String url = URL.replace("%LIMIT%", limit + "");
        url = url.replace("%OFFSET%", offset + "");
        switch(type) {
            case CASE:
                url = url.replace("%COMPONENT_TYPE%", "case");
                break;
            case CPU:
                url = url.replace("%COMPONENT_TYPE%", "processor");
                break;
            case CPU_FAN:
                url = url.replace("%COMPONENT_TYPE%", "cpu_fan");
                break;
            case GPU:
                url = url.replace("%COMPONENT_TYPE%", "gpu");
                break;
            case MEMORY:
                url = url.replace("%COMPONENT_TYPE%", "storage");
                break;
            case MOTHERBOARD:
                url = url.replace("%COMPONENT_TYPE%", "motherboard");
                break;
            case PSU:
                url = url.replace("%COMPONENT_TYPE%", "power_supply");
                break;
            case RAM:
                url = url.replace("%COMPONENT_TYPE%", "ram");
                break;
        }

        return url;
    }

}
