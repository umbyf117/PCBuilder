package com.example.socialgaming.data;

import java.util.Map;

public class CPUFan extends ComponentBase {
    private int rpm;
    private String color;
    private String noiseLevel;

    public CPUFan() {}

    public CPUFan(String id, String title, String link, String img, double price, String brand, String model, int rpm, String color, String noiseLevel) {
        super(id, title, link, img, price, brand, model);
        this.rpm = rpm;
        this.color = color;
        this.noiseLevel = noiseLevel;
    }

    public CPUFan(String id, String title, String link, String img, double price, String brand, String model, String rpm, String color, String noiseLevel) {
        super(id, title, link, img, price, brand, model);
        setRpm(rpm);
        this.color = color;
        this.noiseLevel = noiseLevel;
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> data = super.getMap();
        data.put("rpm", rpm);
        data.put("color", color);
        data.put("noiseLevel", noiseLevel);
        return data;
    }

    private void setRpm(String rpm) {
        this.rpm = Integer.parseInt(rpm.replace(" RPM", ""));
    }

    public int getRpm() {
        return rpm;
    }
    public void setRpm(int rpm) {
        this.rpm = rpm;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getNoiseLevel() {
        return noiseLevel;
    }
    public void setNoiseLevel(String noiseLevel) {
        this.noiseLevel = noiseLevel;
    }
}
/*
id:"B0009FV5PK"
title:"Thermaltake CL-P0187 1U Active Cooling Solution For Intel LGA775"
link:"https://amazon.com/dp/B0009FV5PK?tag=pcbuildcompat-20"
img:"https://m.media-amazon.com/images/I/512OFsDQEtL._SL75_.jpg"
price:32.95
brand:"Thermaltake"
model:"CL-P0187"
rpm:"4800 RPM"
color:"Black"
noiseLevel:"36 dBA"
 */