package com.example.socialgaming.data;

import java.util.Map;

public class Motherboard extends ComponentBase {
    private String formFactor;
    private String chipset;
    private int memorySlots;
    private String socketType;

    public Motherboard() {}

    public Motherboard(String id, String title, String link, String img, double price, String brand, String model, String formFactor, String chipset, int memorySlots, String socketType) {
        super(id, title, link, img, price, brand, model);
        this.formFactor = formFactor;
        this.chipset = chipset;
        this.memorySlots = memorySlots;
        this.socketType = socketType;
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> data = super.getMap();
        data.put("formFactor", formFactor);
        data.put("chipset", chipset);
        data.put("memorySlots", memorySlots);
        data.put("socketType", socketType);
        return data;
    }

    public boolean compatibleCase(Case c) {
        if(formFactor.indexOf("ITX") != -1 && c.getCabinet().indexOf("ITX") != -1)
            return true;
        if(formFactor.indexOf("Micro ATX") != -1 && c.getCabinet().indexOf("MicroATX") != -1)
            return true;
        if(formFactor.equalsIgnoreCase("ATX") && c.getCabinet().indexOf("ATX") == 0)
            return true;
        return false;
    }

    public boolean compatibleCPU(CPU cpu) {
        if(this.chipset.toLowerCase().indexOf(cpu.getBrand().toLowerCase()) != -1)
            return true;
        return false;
    }

    public String getFormFactor() {
        return formFactor;
    }
    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }
    public String getChipset() {
        return chipset;
    }
    public void setChipset(String chipset) {
        this.chipset = chipset;
    }
    public int getMemorySlots() {
        return memorySlots;
    }
    public void setMemorySlots(int memorySlots) {
        this.memorySlots = memorySlots;
    }
    public String getSocketType() {
        return socketType;
    }
    public void setSocketType(String socketType) {
        this.socketType = socketType;
    }
}
/*
id:"cld4mzbod004zsqaojs5k7rjh"
title:"GIGABYTE X570 AORUS Master (AMD Ryzen 3000/X570/ATX/PCIe4.0/DDR4/USB3.1/ESS 9118 Sabre HiFi DAC/Fins-Array Heatsink/RGB Fusion 2.0/3xM.2 Thermal Guard/Gaming Motherboard)"
link:"https://amazon.com/dp/B07SSM6CLC?tag=pcbuildcompat-20"
img:"https://m.media-amazon.com/images/I/51QTivVMbGL._SL75_.jpg"
price:699
brand:"Gigabyte"
model:"X570 AORUS MASTER"
formFactor:"ATX"
chipset:"AMD X570"
memorySlots:"4"
socketType:"AM4"
 */
