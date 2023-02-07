package com.example.socialgaming.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

public class GPU extends ComponentBase  implements Serializable {
    private int memory;
    private String clockSpeed;
    private String chipset;

    public GPU() {}

    public GPU(String id, String title, String link, String img, double price, String brand, String model, int memory, String clockSpeed, String chipset) {
        super(id, title, link, img, price, brand, model);
        this.memory = memory;
        this.clockSpeed = clockSpeed;
        this.chipset = chipset;
    }

    @Override
    public void setJSONData(JSONObject o) throws JSONException {
        super.setJSONData(o);
        setMemoryFromString(o.getString("memory"));
        this.clockSpeed = o.getString("clockSpeed");
        this.chipset = o.getString("chipset");
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> data = super.getMap();
        data.put("memory", memory);
        data.put("clockSpeed", clockSpeed);
        data.put("chipset", chipset);
        return data;
    }

    private void setMemoryFromString(String memory) {
        this.memory = Integer.parseInt(memory.replace(" GB", ""));
    }

    public int getMemory() {
        return memory;
    }
    public void setMemory(int memory) {
        this.memory = memory;
    }
    public String getClockSpeed() {
        return clockSpeed;
    }
    public void setClockSpeed(String clockSpeed) {
        this.clockSpeed = clockSpeed;
    }
    public String getChipset() {
        return chipset;
    }
    public void setChipset(String chipset) {
        this.chipset = chipset;
    }
}
/*
id:"cld4mzbom006zsqaomy10rk60"
title:"ASUS TUF Gaming GeForce RTX® 4090 OC Edition Gaming Graphics Card (PCIe 4.0, 24GB GDDR6X, HDMI 2.1a, DisplayPort 1.4a) TUF-RTX4090-O24G-GAMING"
link:"https://amazon.com/dp/B0BGV6LQYR?tag=pcbuildcompat-20"
img:"https://m.media-amazon.com/images/I/41zEpaKHSoL._SL75_.jpg"
price:2158.14
brand:"ASUS"
model:"TUF Gaming GeForce RTX 4090 OC Edition"
storageInterface:"PCIe 4.0 x16"
memory:"24 GB"
clockSpeed:"2595 MHz"
chipset:"GeForce RTX 4090"
 */
