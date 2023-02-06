package com.example.socialgaming.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

public class Case extends ComponentBase implements Serializable {

    private String sidePanel;
    private String color;
    private String cabinet;

    public Case() {}

    public Case(String id, String title, String link, String img, double price, String brand, String model, String sidePanel, String color, String cabinet) {
        super(id, title, link, img, price, brand, model);
        this.sidePanel = sidePanel;
        this.color = color;
        this.cabinet = cabinet;
    }

    @Override
    public void setJSONData(JSONObject o) throws JSONException {
        super.setJSONData(o);
        this.sidePanel = o.getString("sidePanel");
        this.color = o.getString("color");
        this.cabinet = o.getString("cabinetType");
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> data = super.getMap();
        data.put("sidePanel", sidePanel);
        data.put("color", color);
        data.put("cabinet", cabinet);
        return data;
    }

    public String getSidePanel() {
        return sidePanel;
    }
    public void setSidePanel(String sidePanel) {
        this.sidePanel = sidePanel;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getCabinet() {
        return cabinet;
    }
    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }
}
/*
id:"B00006B6UL"
title:"Antec Performance Series P120 Crystal E-ATX Mid-Tower Case, Tempered Glass Front & Side Panels White Led USB3.0 X 2, Aluminum Vga Holder Included - PC"
link:"https://amazon.com/dp/B00006B6UL?tag=pcbuildcompat-20"
img:"https://m.media-amazon.com/images/I/41nDuZNb2ML._SL75_.jpg"
price:117
brand:"Antec"
model:"P120 Crystal"
sidePanel:"Tempered Glass"
color:"Black"
cabinetType:"ATX Mid Tower"
 */
