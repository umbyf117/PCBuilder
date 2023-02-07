package com.example.socialgaming.data;

import android.graphics.Bitmap;

import com.example.socialgaming.utils.ImageUtils;

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

        for(RAM r : build.getRams()) {
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
        
        for(Memory m : build.getHarddisks()) {
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
    public BuildFirestore() {}

    public Map<String, Object> getAttributeMap() {
        Map<String, Object> attributeMap = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(!field.getName().equals("image"))
            try {
                field.setAccessible(true);
                attributeMap.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        attributeMap.put("image", ImageUtils.encodeArrayToList(ImageUtils.encodeBitmapToByteArray(image)));
        return attributeMap;
    }

    public void setAttributes(Map<String, Object> attributeMap) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(!field.getName().equals("image"))
            try {
                field.setAccessible(true);
                field.set(this, attributeMap.get(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        this.image = ImageUtils.decodeByteArrayToBitmap(ImageUtils.decodeListToArray((List<Long>) attributeMap.get("image")));
    }

    //LIKE DISLIKE METHODS
    public boolean addLike(String username) {
        if(like.contains(username))
            return false;
        if(dislike.contains(username))
            dislike.remove(username);
        like.add(username);
        return true;
    }
    public boolean addDislike(String username) {
        if(dislike.contains(username))
            return false;
        if(like.contains(username))
            like.remove(username);
        dislike.add(username);
        return true;
    }
    public boolean removeLike(String username) {
        if(!like.contains(username))
            return false;
        like.remove(username);
        return true;
    }
    public boolean removeDislike(String username) {
        if(!dislike.contains(username))
            return false;
        dislike.remove(username);
        return true;
    }
    public void switchLikes(String username) {
        if(like.contains(username)) {
            like.remove(username);
            return;
        }
        if(dislike.contains(username))
            dislike.remove(username);
        like.add(username);
    }
    public void switchDislikes(String username) {
        if(dislike.contains(username)) {
            dislike.remove(username);
            return;
        }
        if(like.contains(username))
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
        if(like.size() == 0)
            return 0;

        if(dislike.size() == 0)
            return 100.0;

        double value = (like.size() * 10)/(dislike.size() * 10);
        return value/10;
    }

    public String getName() {
        return name;
    }
    public String getCreator() {
        return name;
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