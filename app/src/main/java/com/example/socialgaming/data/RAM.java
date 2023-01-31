package com.example.socialgaming.data;

import com.example.socialgaming.data.types.RamType;

import org.json.JSONException;
import org.json.JSONObject;

public class RAM extends ComponentBase {

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

    @Override
    public void getJSONData(JSONObject o) throws JSONException {
        super.getJSONData(o);
        setSize(o.getString("size"));
        setQuantity(o.getString("quantity"));
        setType(o.getString("type"));
    }

    private void setSize(String size) {
        this.size = Integer.parseInt(size.replace(" GB", ""));
    }

    private void setQuantity(String quantity) {
        this.quantity = Integer.parseInt(quantity.substring(0, quantity.indexOf(" x ")));
    }

    private void setType(String type) {
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
