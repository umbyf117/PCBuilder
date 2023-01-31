package com.example.socialgaming.data;

import org.json.JSONException;
import org.json.JSONObject;

public class PSU extends ComponentBase {

    private int power;
    private String color;
    private String efficiency;

    public PSU() {}

    public PSU(String id, String title, String link, String img, double price, String brand, String model, int power, String color, String efficiency) {
        super(id, title, link, img, price, brand, model);
        this.power = power;
        this.color = color;
        this.efficiency = efficiency;
    }

    @Override
    public void getJSONData(JSONObject o) throws JSONException {
        super.getJSONData(o);
        this.color = o.getString("color");
        this.efficiency = o.getString("efficiency");
        setPower(o.getString("power"));
    }

    private void setPower(String power) {
        this.power = Integer.parseInt(power.replace(" W", ""));
    }

    public int getPower() {
        return power;
    }
    public void setPower(int power) {
        this.power = power;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getEfficiency() {
        return efficiency;
    }
    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }
}
/*
id:"cld4mzboz00iisqaotef9s78n"
title:"Corsair RM Series, RM750, 750 Watt, 80+ Gold Certified, Fully Modular Power Supply, Microsoft Modern Standby"
link:"https://amazon.com/dp/B07RF237B1?tag=pcbuildcompat-20"
img:"https://m.media-amazon.com/images/I/51EkSdu6J-L._SL75_.jpg"
price:219
brand:"Corsair"
model:"RM750"
power:"750 W"
color:"Black"
efficiency:"80+ Gold"
 */
