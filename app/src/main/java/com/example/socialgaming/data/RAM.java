package com.example.socialgaming.data;

import com.example.socialgaming.data.types.RamType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

public class RAM extends ComponentBase  implements Serializable {

    private int size;
    private int quantity;
    private RamType type;

    public RAM() {}

    public RAM(String id, String title, String link, String img, double price, String brand, String model, int size, int quantity, RamType type) {
        super(id, title, link, img, price, brand, model);
        this.size = size;
        this.quantity = quantity;
        this.type = type;
    }

    public RAM(String id, String title, String link, String img, double price, String brand, String model, String size, String quantity, RamType type) {
        super(id, title, link, img, price, brand, model);
        setSizeFromString(size);
        setQuantityFromString(quantity);
        this.type = type;
    }

    @Override
    public void setJSONData(JSONObject o) throws JSONException {
        super.setJSONData(o);
        setSizeFromString(o.getString("size"));
        setQuantityFromString(o.getString("quantity"));
        setTypeFromString(o.getString("type"));
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> data = super.getMap();
        data.put("size", size);
        data.put("quantity", quantity);
        data.put("RamType", type);
        return data;
    }

    private void setSizeFromString(String size) {
        this.size = Integer.parseInt(size.replace(" GB", ""));
    }

    private void setQuantityFromString(String quantity) {
        this.quantity = Integer.parseInt(quantity.substring(0, quantity.indexOf(" x ")));
    }

    private void setTypeFromString(String type) {
        if(type.equalsIgnoreCase("DDR3"))
            this.type = RamType.DDR3;
        else if(type.equalsIgnoreCase("DDR4"))
            this.type = RamType.DDR4;
        else if(type.equalsIgnoreCase("DDR5"))
            this.type = RamType.DDR5;
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public RamType getType() {
        return type;
    }
    public void setType(RamType type) {
        this.type = type;
    }
}
/*
id:"cld4mzbqc01r5sqaox3gky7jn"
title:"Corsair Vengeance LPX 16GB (2x8GB) DDR4 DRAM 3200MHz C16 Desktop Memory Kit - Black (CMK16GX4M2B3200C16)"
link:"https://amazon.com/dp/B0143UM4TC?tag=pcbuildcompat-20"
img:"https://m.media-amazon.com/images/I/418sCJkoOYL._SL75_.jpg"
price:49.99
brand:"Corsair"
model:"Vengeance LPX"
size:"16 GB"
quantity:"2 x 8 GB"
type:"DDR4"
 */
