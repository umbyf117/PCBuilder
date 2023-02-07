package com.example.socialgaming.data;

import com.example.socialgaming.data.types.MemoryType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

public class Memory extends ComponentBase  implements Serializable {
    private String rpm;
    private String cache;
    private MemoryType type;

    public Memory() {}

    public Memory(String id, String title, String link, String img, double price, String brand, String model, String rpm, String cache, MemoryType type) {
        super(id, title, link, img, price, brand, model);
        this.rpm = rpm;
        this.cache = cache;
        this.type = type;
    }

    @Override
    public void setJSONData(JSONObject o) throws JSONException {
        super.setJSONData(o);
        this.rpm = o.getString("rpm");
        this.cache = o.getString("cacheMemory");
        setTypeFromString(o.getString("type"));
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> data = super.getMap();
        data.put("rpm", rpm);
        data.put("cache", cache);
        data.put("type", type);
        return data;
    }

    private void setTypeFromString(String type) {
        if(type.indexOf("SSD") != -1)
            this.type = MemoryType.SSD;
        else
            this.type = MemoryType.HDD;
    }

    public int getGBRpm() {
        if(rpm.indexOf("TB") != -1) {
            return Integer.parseInt(rpm.substring(0, rpm.indexOf("TB") - 1)) * 1000;
        }
        return Integer.parseInt(rpm.substring(0, rpm.indexOf("GB") - 1));
    }

    public String getRpm() {
        return rpm;
    }
    public void setRpm(String rpm) {
        this.rpm = rpm;
    }
    public String getCache() {
        return cache;
    }
    public void setCache(String cache) {
        this.cache = cache;
    }
    public MemoryType getType() {
        return type;
    }
    public void setType(MemoryType type) {
        this.type = type;
    }
}
/*
id:"cld4mzbqj026vsqaoh1ma2yn3"
title:"SAMSUNG (MZ-V7E1T0BW) 970 EVO SSD 1TB - M.2 NVMe Interface Internal Solid State Drive With V-NAND Technology, Black/Red"
link:"https://amazon.com/dp/B07BN217QG?tag=pcbuildcompat-20"
img:"https://m.media-amazon.com/images/I/31SSaevqVNL._SL75_.jpg"
price:179.99
brand:"Samsung"
model:"970 EVO"
storageInterface:"PCIe 3.0 x4"
rpm:"1 TB"
type:"SSD"
cacheMemory:"1024 MB"
 */