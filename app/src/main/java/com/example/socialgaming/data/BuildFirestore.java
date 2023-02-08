package com.example.socialgaming.data;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.socialgaming.utils.ImageUtils;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BuildFirestore {
    private String name, creator;
    private UUID uuid;
    private Bitmap image;
    private List<String> like, dislike;

    private String houseTitle, cpuTitle, fanTitle, gpuTitle, boardTitle, psuTitle;
    private List<String> ramsTitle, memoriesTitle;
    private double housePrice, cpuPrice, fanPrice, gpuPrice, boardPrice, psuPrice;
    private List<Double> ramsPrice, memoriesPrice;

    private String formFactor;
    private String speedCpu, chipset;
    private String rpmFan;
    private String memoryGpu, speedGpu;
    private List<String> memoriesType;
    private List<Integer> memoriesDimension;
    private String powerPsu;
    private List<Integer> sizeRams, quantityRams;
    private List<String> ramsType;

    public BuildFirestore(Build build) {
        this.name = build.getName();
        this.creator = build.getCreator();
        this.uuid = build.getUuid();
        this.image = build.getImage();
        this.like = build.getLike();
        this.dislike = build.getDislike();

        //INFORMAZIONI MOTHERBOARD
        Motherboard board = build.getBoard();
        this.boardTitle = board.getBrand() + " " + board.getModel();
        this.boardPrice = board.getPrice();
        this.formFactor = board.getFormFactor();

        //INFORMAZIONI CPU
        CPU cpu = build.getCpu();
        this.cpuTitle = cpu.getBrand() + " " + cpu.getModel();
        this.cpuPrice = cpu.getPrice();
        this.speedCpu = cpu.getSpeed();
        this.chipset = cpu.getBrand();

        //INFORMAZIONI CPU FAN
        CPUFan fan = build.getFan();
        this.fanTitle = fan.getBrand() + " " + fan.getModel();
        this.fanPrice = fan.getPrice();
        this.rpmFan = fan.getRpm();

        //INFORMAZIONI GPU
        GPU gpu = build.getGpu();
        this.gpuTitle = gpu.getBrand() + " " + gpu.getModel();
        this.gpuPrice = gpu.getPrice();
        this.memoryGpu = gpu.getMemory() + " GB";
        this.speedGpu = gpu.getClockSpeed();

        //INFORMAZIONI PSU
        PSU psu = build.getPsu();
        this.psuTitle = psu.getTitle();
        this.psuPrice = psu.getPrice();
        this.powerPsu = psu.getPower() + " W";

        //INFORMAZIONI CASE
        Case house = build.getHouse();
        this.houseTitle = house.getBrand() + " " + house.getModel();
        this.housePrice = house.getPrice();

        //INFORMAZIONI RAM
        this.ramsTitle = new ArrayList<>();
        this.ramsPrice = new ArrayList<>();
        this.quantityRams = new ArrayList<>();
        this.sizeRams = new ArrayList<>();
        this.ramsType = new ArrayList<>();

        for (RAM r : build.getRams()) {
            this.ramsTitle.add(r.getBrand() + " " + r.getModel());
            this.ramsPrice.add(r.getPrice());
            this.quantityRams.add(r.getQuantity());
            this.sizeRams.add(r.getSize());
            this.ramsType.add(r.getType().toString());
        }

        //INFORMAZIONI HARD DISK
        this.memoriesTitle = new ArrayList<>();
        this.memoriesPrice = new ArrayList<>();
        this.memoriesDimension = new ArrayList<>();
        this.memoriesType = new ArrayList<>();

        for (Memory m : build.getHarddisks()) {
            this.memoriesTitle.add(m.getBrand() + " " + m.getTitle());
            this.memoriesPrice.add(m.getPrice());
            this.memoriesType.add(m.getType().toString());
            this.memoriesDimension.add(m.getGBRpm());
        }

    }

    public BuildFirestore(Map<String, Object> map) {
        this();
        this.setAttributes(map);
    }

    public BuildFirestore() {
    }

    public void updateWithDocument(DocumentSnapshot documentSnapshot) {
        this.setAttributes(documentSnapshot.getData());
    }

    //MAPS
    public Map<String, Object> getAttributeMap() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", name);
        attributes.put("creator", creator);
        attributes.put("uuid", uuid.toString());
        attributes.put("like", like);
        attributes.put("dislike", dislike);
        attributes.put("houseTitle", houseTitle);
        attributes.put("cpuTitle", cpuTitle);
        attributes.put("fanTitle", fanTitle);
        attributes.put("gpuTitle", gpuTitle);
        attributes.put("boardTitle", boardTitle);
        attributes.put("psuTitle", psuTitle);
        attributes.put("housePrice", housePrice);
        attributes.put("cpuPrice", cpuPrice);
        attributes.put("fanPrice", fanPrice);
        attributes.put("gpuPrice", gpuPrice);
        attributes.put("boardPrice", boardPrice);
        attributes.put("psuPrice", psuPrice);
        attributes.put("formFactor", formFactor);
        attributes.put("speedCpu", speedCpu);
        attributes.put("chipset", chipset);
        attributes.put("rpmFan", rpmFan);
        attributes.put("memoryGpu", memoryGpu);
        attributes.put("speedGpu", speedGpu);
        attributes.put("powerPsu", powerPsu);
        attributes.put("ramsTitle", ramsTitle);
        attributes.put("ramsPrice", ramsPrice);
        attributes.put("sizeRams", sizeRams);
        attributes.put("quantityRams", quantityRams);
        attributes.put("ramsType", ramsType);
        attributes.put("memoriesTitle", memoriesTitle);
        attributes.put("memoriesPrice", memoriesPrice);
        attributes.put("memoriesType", memoriesType);
        attributes.put("memoriesDimension", memoriesDimension);

        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        memoriesTitle = (List<String>) attributes.get("memoriesTitle");
        memoriesPrice = (List<Double>) attributes.get("memoriesPrice");
        memoriesType = (List<String>) attributes.get("memoriesType");
        memoriesDimension = (List<Integer>) attributes.get("memoriesDimension");
        this.ramsTitle = (List<String>) attributes.get("ramsTitle");
        this.ramsPrice = (List<Double>) attributes.get("ramsPrice");
        this.sizeRams = (List<Integer>) attributes.get("sizeRams");
        this.quantityRams = (List<Integer>) attributes.get("quantityRams");
        this.ramsType = (List<String>) attributes.get("ramsType");
        this.name = (String) attributes.get("name");
        this.creator = (String) attributes.get("creator");
        this.uuid = UUID.fromString((String) attributes.get("uuid"));
        this.like = (List<String>) attributes.get("like");
        this.dislike = (List<String>) attributes.get("dislike");
        this.houseTitle = (String) attributes.get("houseTitle");
        this.cpuTitle = (String) attributes.get("cpuTitle");
        this.fanTitle = (String) attributes.get("fanTitle");
        this.gpuTitle = (String) attributes.get("gpuTitle");
        this.boardTitle = (String) attributes.get("boardTitle");
        this.psuTitle = (String) attributes.get("psuTitle");
        this.housePrice = (double) attributes.get("housePrice");
        this.cpuPrice = (double) attributes.get("cpuPrice");
        this.fanPrice = (double) attributes.get("fanPrice");
        this.gpuPrice = (double) attributes.get("gpuPrice");
        this.boardPrice = (double) attributes.get("boardPrice");
        this.psuPrice = (double) attributes.get("psuPrice");
        this.formFactor = (String) attributes.get("formFactor");
        this.speedCpu = (String) attributes.get("speedCpu");
        this.chipset = (String) attributes.get("chipset");
        this.rpmFan = (String) attributes.get("rpmFan");
        this.memoryGpu = (String) attributes.get("memoryGpu");
        this.speedGpu = (String) attributes.get("speedGpu");
        this.powerPsu = (String) attributes.get("powerPsu");

    }

    public Map<String, Object> getMemoryAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put("memoriesTitle", memoriesTitle);
        map.put("memoriesPrice", memoriesPrice);
        map.put("memoriesType", memoriesType);
        map.put("memoriesDimension", memoriesDimension);
        return map;
    }

    public void setMemoryAttributes(Map<String, Object> map) {
        memoriesTitle = (List<String>) map.get("memoriesTitle");
        memoriesPrice = (List<Double>) map.get("memoriesPrice");
        memoriesType = (List<String>) map.get("memoriesType");
        memoriesDimension = (List<Integer>) map.get("memoriesDimension");
    }

    public Map<String, Object> getRamsAttributes() {
        Map<String, Object> ramsAttributes = new HashMap<>();
        ramsAttributes.put("ramsTitle", ramsTitle);
        ramsAttributes.put("ramsPrice", ramsPrice);
        ramsAttributes.put("sizeRams", sizeRams);
        ramsAttributes.put("quantityRams", quantityRams);
        ramsAttributes.put("ramsType", ramsType);
        return ramsAttributes;
    }

    public void setRamsAttributes(Map<String, Object> ramsAttributes) {
        this.ramsTitle = (List<String>) ramsAttributes.get("ramsTitle");
        this.ramsPrice = (List<Double>) ramsAttributes.get("ramsPrice");
        this.sizeRams = (List<Integer>) ramsAttributes.get("sizeRams");
        this.quantityRams = (List<Integer>) ramsAttributes.get("quantityRams");
        this.ramsType = (List<String>) ramsAttributes.get("ramsType");
    }

    public Map<String, Object> getBaseAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", name);
        attributes.put("creator", creator);
        attributes.put("uuid", uuid.toString());
        attributes.put("like", like);
        attributes.put("dislike", dislike);
        attributes.put("houseTitle", houseTitle);
        attributes.put("cpuTitle", cpuTitle);
        attributes.put("fanTitle", fanTitle);
        attributes.put("gpuTitle", gpuTitle);
        attributes.put("boardTitle", boardTitle);
        attributes.put("psuTitle", psuTitle);
        attributes.put("housePrice", housePrice);
        attributes.put("cpuPrice", cpuPrice);
        attributes.put("fanPrice", fanPrice);
        attributes.put("gpuPrice", gpuPrice);
        attributes.put("boardPrice", boardPrice);
        attributes.put("psuPrice", psuPrice);
        attributes.put("formFactor", formFactor);
        attributes.put("speedCpu", speedCpu);
        attributes.put("chipset", chipset);
        attributes.put("rpmFan", rpmFan);
        attributes.put("memoryGpu", memoryGpu);
        attributes.put("speedGpu", speedGpu);
        attributes.put("powerPsu", powerPsu);
        return attributes;
    }

    public void setBaseAttributes(Map<String, Object> attributes) {
        this.name = (String) attributes.get("name");
        this.creator = (String) attributes.get("creator");
        this.uuid = UUID.fromString((String) attributes.get("uuid"));
        this.like = (List<String>) attributes.get("like");
        this.dislike = (List<String>) attributes.get("dislike");
        this.houseTitle = (String) attributes.get("houseTitle");
        this.cpuTitle = (String) attributes.get("cpuTitle");
        this.fanTitle = (String) attributes.get("fanTitle");
        this.gpuTitle = (String) attributes.get("gpuTitle");
        this.boardTitle = (String) attributes.get("boardTitle");
        this.psuTitle = (String) attributes.get("psuTitle");
        this.housePrice = (double) attributes.get("housePrice");
        this.cpuPrice = (double) attributes.get("cpuPrice");
        this.fanPrice = (double) attributes.get("fanPrice");
        this.gpuPrice = (double) attributes.get("gpuPrice");
        this.boardPrice = (double) attributes.get("boardPrice");
        this.psuPrice = (double) attributes.get("psuPrice");
        this.formFactor = (String) attributes.get("formFactor");
        this.speedCpu = (String) attributes.get("speedCpu");
        this.chipset = (String) attributes.get("chipset");
        this.rpmFan = (String) attributes.get("rpmFan");
        this.memoryGpu = (String) attributes.get("memoryGpu");
        this.speedGpu = (String) attributes.get("speedGpu");
        this.powerPsu = (String) attributes.get("powerPsu");
    }

    /*
    //PRICE METHOD
    public double getTotalPrice() {
        double totPrice;
        totPrice = boardPrice + cpuPrice + totRamsPrice() + fanPrice + gpuPrice + totMemoriesPrice() +
                psuPrice + housePrice;

        return totPrice;
    }

    public double totRamsPrice() {
        Double totRamPrice = 0.0;
        for (double ram: ramsPrice) {
            totRamPrice += ram;
        }
        return totRamPrice;
    }

    public double totMemoriesPrice() {
        Double totMemoriesPrice = 0.0;
        for (double memory: memoriesPrice) {
            totMemoriesPrice += memory;
        }
        return totMemoriesPrice;
    }
    */

    //LIKE DISLIKE METHODS
    public boolean addLike(String username) {
        if (like.contains(username))
            return false;
        if (dislike.contains(username))
            dislike.remove(username);
        like.add(username);
        return true;
    }

    public boolean addDislike(String username) {
        if (dislike.contains(username))
            return false;
        if (like.contains(username))
            like.remove(username);
        dislike.add(username);
        return true;
    }

    public boolean removeLike(String username) {
        if (!like.contains(username))
            return false;
        like.remove(username);
        return true;
    }

    public boolean removeDislike(String username) {
        if (!dislike.contains(username))
            return false;
        dislike.remove(username);
        return true;
    }

    public void switchLikes(String username) {
        if (like.contains(username)) {
            like.remove(username);
            return;
        }
        if (dislike.contains(username))
            dislike.remove(username);
        like.add(username);
    }

    public void switchDislikes(String username) {
        if (dislike.contains(username)) {
            dislike.remove(username);
            return;
        }
        if (like.contains(username))
            like.remove(username);
        dislike.add(username);
    }

    public int getLikes() {
        return like.size();
    }

    public int getDislikes() {
        return dislike.size();
    }

    public double getValue() {
        if (like.size() == 0)
            return 0;

        if (dislike.size() == 0)
            return 100.0;

        double value = (like.size() * 10) / (dislike.size() * 10);
        return value / 10;
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Bitmap getImage() {
        return image;
    }

    public List<String> getLike() {
        return like;
    }

    public List<String> getDislike() {
        return dislike;
    }

    public String getHouseTitle() {
        return houseTitle;
    }

    public String getCpuTitle() {
        return cpuTitle;
    }

    public String getFanTitle() {
        return fanTitle;
    }

    public String getGpuTitle() {
        return gpuTitle;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public String getPsuTitle() {
        return psuTitle;
    }

    public List<String> getRamsTitle() {
        return ramsTitle;
    }

    public List<String> getMemoriesTitle() {
        return memoriesTitle;
    }

    public double getHousePrice() {
        return housePrice;
    }

    public double getCpuPrice() {
        return cpuPrice;
    }

    public double getFanPrice() {
        return fanPrice;
    }

    public double getGpuPrice() {
        return gpuPrice;
    }

    public double getBoardPrice() {
        return boardPrice;
    }

    public double getPsuPrice() {
        return psuPrice;
    }

    public List<Double> getRamsPrice() {
        return ramsPrice;
    }

    public List<Double> getMemoriesPrice() {
        return memoriesPrice;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public String getSpeedCpu() {
        return speedCpu;
    }

    public String getChipset() {
        return chipset;
    }

    public String getRpmFan() {
        return rpmFan;
    }

    public String getMemoryGpu() {
        return memoryGpu;
    }

    public String getSpeedGpu() {
        return speedGpu;
    }

    public List<String> getMemoriesType() {
        return memoriesType;
    }

    public List<Integer> getMemoriesDimension() {
        return memoriesDimension;
    }

    public String getPowerPsu() {
        return powerPsu;
    }

    public List<Integer> getSizeRams() {
        return sizeRams;
    }

    public List<Integer> getQuantityRams() {
        return quantityRams;
    }

    public List<String> getRamsType() {
        return ramsType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setLike(List<String> like) {
        this.like = like;
    }

    public void setDislike(List<String> dislike) {
        this.dislike = dislike;
    }

    public void setHouseTitle(String houseTitle) {
        this.houseTitle = houseTitle;
    }

    public void setCpuTitle(String cpuTitle) {
        this.cpuTitle = cpuTitle;
    }

    public void setFanTitle(String fanTitle) {
        this.fanTitle = fanTitle;
    }

    public void setGpuTitle(String gpuTitle) {
        this.gpuTitle = gpuTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public void setPsuTitle(String psuTitle) {
        this.psuTitle = psuTitle;
    }

    public void setRamsTitle(List<String> ramsTitle) {
        this.ramsTitle = ramsTitle;
    }

    public void setMemoriesTitle(List<String> memoriesTitle) {
        this.memoriesTitle = memoriesTitle;
    }

    public void setHousePrice(double housePrice) {
        this.housePrice = housePrice;
    }

    public void setCpuPrice(double cpuPrice) {
        this.cpuPrice = cpuPrice;
    }

    public void setFanPrice(double fanPrice) {
        this.fanPrice = fanPrice;
    }

    public void setGpuPrice(double gpuPrice) {
        this.gpuPrice = gpuPrice;
    }

    public void setBoardPrice(double boardPrice) {
        this.boardPrice = boardPrice;
    }

    public void setPsuPrice(double psuPrice) {
        this.psuPrice = psuPrice;
    }

    public void setRamsPrice(List<Double> ramsPrice) {
        this.ramsPrice = ramsPrice;
    }

    public void setMemoriesPrice(List<Double> memoriesPrice) {
        this.memoriesPrice = memoriesPrice;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public void setSpeedCpu(String speedCpu) {
        this.speedCpu = speedCpu;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public void setRpmFan(String rpmFan) {
        this.rpmFan = rpmFan;
    }

    public void setMemoryGpu(String memoryGpu) {
        this.memoryGpu = memoryGpu;
    }

    public void setSpeedGpu(String speedGpu) {
        this.speedGpu = speedGpu;
    }

    public void setMemoriesType(List<String> memoriesType) {
        this.memoriesType = memoriesType;
    }

    public void setMemoriesDimension(List<Integer> memoriesDimension) {
        this.memoriesDimension = memoriesDimension;
    }

    public void setPowerPsu(String powerPsu) {
        this.powerPsu = powerPsu;
    }

    public void setSizeRams(List<Integer> sizeRams) {
        this.sizeRams = sizeRams;
    }

    public void setQuantityRams(List<Integer> quantityRams) {
        this.quantityRams = quantityRams;
    }

    public void setRamsType(List<String> ramsType) {
        this.ramsType = ramsType;
    }
}