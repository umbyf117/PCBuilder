package com.example.socialgaming.data;

import com.example.socialgaming.data.types.ComponentType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ComponentBase {
    private String id;
    private String title;
    private String link;
    private String img;
    private double price;
    private String brand;
    private String model;

    public ComponentBase() {}

    public ComponentBase(String id, String title, String link, String img, double price, String brand, String model) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.img = img;
        this.price = price;
        this.brand = brand;
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ComponentBase))
            return false;
        ComponentBase componentBase = (ComponentBase) o;
        return this.id.equals(componentBase.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, link, img, price, brand, model);
    }

    public void setJSONData(JSONObject o) throws JSONException {
        this.id = o.getString("id");
        this.title = o.getString("title");
        this.link = o.getString("link");
        this.img = o.getString("img");
        this.price = o.getDouble("price");
        this.brand = o.getString("brand");
        this.model = o.getString("model");
    }

    public Map<String, Object> getMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("title",title);
        data.put("link",link);
        data.put("image", img);
        data.put("price",price);
        data.put("brand",brand);
        data.put("model",model);
        return data;
    }

    public static ComponentType getComponentType(ComponentBase component) {
        if(component instanceof Case)
            return ComponentType.CASE;
        if(component instanceof CPU)
            return ComponentType.CPU;
        if(component instanceof CPUFan)
            return ComponentType.CPU_FAN;
        if(component instanceof GPU)
            return ComponentType.GPU;
        if(component instanceof Memory)
            return ComponentType.MEMORY;
        if(component instanceof Motherboard)
            return ComponentType.MOTHERBOARD;
        if(component instanceof PSU)
            return ComponentType.PSU;
        if(component instanceof RAM)
            return ComponentType.RAM;
        return null;
    }
    public static ComponentBase construct(ComponentType type) {
        ComponentBase component;
        switch(type) {
            case CASE:
                component = new Case();
                break;
            case CPU:
                component = new CPU();
                break;
            case CPU_FAN:
                component = new CPUFan();
                break;
            case GPU:
                component = new GPU();
                break;
            case MEMORY:
                component = new Memory();
                break;
            case MOTHERBOARD:
                component = new Motherboard();
                break;
            case PSU:
                component = new PSU();
                break;
            case RAM:
                component = new RAM();
                break;
            default:
                component = new ComponentBase();
        }
        return component;
    }

    public static  ComponentBase construct(ComponentType type, Map<String, Object> data) {
        ComponentBase component;
        switch(type) {
            case CASE:
                component = new Case();
                break;
            case CPU:
                component = new CPU();
                break;
            case CPU_FAN:
                component = new CPUFan();
                break;
            case GPU:
                component = new GPU();
                break;
            case MEMORY:
                component = new Memory();
                break;
            case MOTHERBOARD:
                component = new Motherboard();
                break;
            case PSU:
                component = new PSU();
                break;
            case RAM:
                component = new RAM();
                break;
            default:
                component = new ComponentBase();
        }
        try {
            component.setJSONData(new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return component;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
}

/*
[
  {
    "id": "cld4mzboz00iisqaotef9s78n",
    "title": "Corsair RM Series, RM750, 750 Watt, 80+ Gold Certified, Fully Modular Power Supply, Microsoft Modern Standby",
    "link": "https://amazon.com/dp/B07RF237B1?tag=pcbuildcompat-20",
    "img": "https://m.media-amazon.com/images/I/51EkSdu6J-L._SL75_.jpg",
    "price": 219,
    "brand": "Corsair",
    "model": "RM750",
    "power": "750 W",
    "color": "Black",
    "efficiency": "80+ Gold"
  },
  {
    "id": "cld4mzbp000issqaoxyjx5ffi",
    "title": "Corsair RM Series, RM850, 850 Watt, 80+ Gold Certified, Fully Modular Power Supply, Microsoft Modern Standby (CP-9020196-NA)",
    "link": "https://amazon.com/dp/B07RCKG95L?tag=pcbuildcompat-20",
    "img": "https://m.media-amazon.com/images/I/51SoQX-iCnL._SL75_.jpg",
    "price": 214.99,
    "brand": "Corsair",
    "model": "RM850",
    "power": "850 W",
    "color": "Black",
    "efficiency": "80+ Gold"
  },
  {
    "id": "cld4mzbp000itsqaomib1tenu",
    "title": "Corsair CX Series 650 Watt 80 Plus Bronze Certified Modular Power Supply (CP-9020103-NA)",
    "link": "https://amazon.com/dp/B01B72W1VA?tag=pcbuildcompat-20",
    "img": "https://m.media-amazon.com/images/I/41h2NmpC0DL._SL75_.jpg",
    "price": 179.99,
    "brand": "Corsair",
    "model": "CX 650",
    "power": "650 W",
    "color": "Black",
    "efficiency": "80+ Bronze"
  },
  {
    "id": "cld4mzbp000iusqaousczgc2j",
    "title": "EVGA 110-BQ-0500-K1, 500 Bq, 80+ Bronze 500W, Semi Modular, FDB Fan, 3 Year Warranty, Power Supply",
    "link": "https://amazon.com/dp/B01N3OAFHD?tag=pcbuildcompat-20",
    "img": "https://m.media-amazon.com/images/I/51fba2wglGL._SL75_.jpg",
    "price": 98.99,
    "brand": "EVGA",
    "model": "BQ500",
    "power": "500 W",
    "color": "Black",
    "efficiency": "80+ Bronze"
  },
  {
    "id": "cld4mzbp000ivsqao19irja7n",
    "title": "Corsair CX Series 550 Watt 80 Plus Bronze Certified Modular Power Supply (CP-9020102-NA)",
    "link": "https://amazon.com/dp/B01B72W0A2?tag=pcbuildcompat-20",
    "img": "https://m.media-amazon.com/images/I/41O5gVNo3HL._SL75_.jpg",
    "price": 179.99,
    "brand": "Corsair",
    "model": "CX",
    "power": "550 W",
    "color": "Black",
    "efficiency": "80+ Bronze"
  }
]
*/