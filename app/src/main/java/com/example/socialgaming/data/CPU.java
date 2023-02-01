package com.example.socialgaming.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class CPU extends ComponentBase {
    private String speed;
    private String socketType;

    public CPU() {}

    public CPU(String id, String title, String link, String img, double price, String brand, String model, String speed, String socketType) {
        super(id, title, link, img, price, brand, model);
        this.speed = speed;
        this.socketType = socketType;
    }

    @Override
    public void setJSONData(JSONObject o) throws JSONException {
        super.setJSONData(o);
        this.speed = o.getString("speed");
        this.socketType = o.getString("socketType");
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> data = super.getMap();
        data.put("speed", speed);
        data.put("socketType", socketType);
        return data;
    }

    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }
    public String getSocketType() {
        return socketType;
    }
    public void setSocketType(String socketType) {
        this.socketType = socketType;
    }
}
/*
id:"cld4mzblr0000sqao1czxno5m"
title:"AMD Ryzen Threadripper 3990X 64-Core, 128-Thread Unlocked Desktop Processor"
link:"https://amazon.com/dp/B0815SBQ9W?tag=pcbuildcompat-20"
img:"https://m.media-amazon.com/images/I/41cAAdXKoeL._SL75_.jpg"
price:7994.99
brand:"AMD"
model:"Ryzen Threadripper 3990X"
speed:"4.3 GHz"
socketType:"sTRX4"
 */
