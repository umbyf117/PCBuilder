package com.example.socialgaming.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.socialgaming.data.*;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.repository.callbacks.IComponentCallback;
import com.example.socialgaming.ui.Lists.ComponentsFragment;
import com.example.socialgaming.utils.wrapper.ResponseWrapper;
import com.example.socialgaming.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BuildUtils {

    private static final String URL = "https://computer-components-api.p.rapidapi.com/%COMPONENT_TYPE%?limit=%LIMIT%&offset=%OFFSET%";

    //Da usare quando cerco un componente
    public static String getComponentsJSON(ComponentType type, int limit, int offset) {

        ResponseWrapper responseWrapper = new ResponseWrapper();

        Thread backgroundThread = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://computer-components-api.p.rapidapi.com/" + getUrl(type) + "?limit=" + limit + "&offset=" + offset)
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
                        responseWrapper.response = responseBody.string();
                        Log.e("ComponetRead", responseBody.string());
                    }
                }
            });
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
                for (int i = 0; i < obs.length(); i++) {
                    JSONObject obj = obs.getJSONObject(i);
                    ComponentBase component = getComponent(obj, type);
                    components[i] = component;
                }
                return components;

            } catch (final JSONException e) {
                Log.e(MainActivity.class.getSimpleName(), "Json parsing error: " + e.getMessage());
            }
        }

        return null;
    }

    public static ComponentBase getComponent(JSONObject obj, ComponentType type) {

        ComponentBase component = ComponentBase.construct(type);

        try {
            component.setJSONData(obj);
        } catch (JSONException e) {
            Log.e(MainActivity.class.getSimpleName(), "Json getting data error: " + e.getMessage());
        }

        return component;
    }

    public static Map<String, Object> toMap(JSONObject jsonobj)  throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keys = jsonobj.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            Object value = jsonobj.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }   return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }   return list;
    }

    public static String getUrl(ComponentType type) {
        switch (type) {
            case CASE:
                return "case";
            case CPU:
                return "processor";
            case CPU_FAN:
                return "cpu_fan";
            case GPU:
                return "gpu";
            case MEMORY:
                return "storage";
            case MOTHERBOARD:
                return "motherboard";
            case PSU:
                return "power_supply";
            case RAM:
                return "ram";
        }

        return "";
    }

}
