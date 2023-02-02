package com.example.socialgaming.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Build {
    private Motherboard board;
    private CPU cpu;
    private List<RAM> rams;
    private List<Memory> harddisks;
    private GPU gpu;
    private Case house;
    private CPUFan fan;
    private PSU psu;

    private User creator;
    private Set<String> like, dislike;

    public Build(Motherboard board, CPU cpu, List<RAM> rams, List<Memory> harddisks, GPU gpu, Case house, PSU psu, User creator) {
        this.board = board;
        this.cpu = cpu;
        this.rams = rams;
        this.harddisks = harddisks;
        this.gpu = gpu;
        this.house = house;
        this.psu = psu;
        this.creator = creator;
        this.like = new HashSet<>();
        this.dislike = new HashSet<>();
    }

    public Build(Motherboard board, CPU cpu, List<RAM> rams, List<Memory> harddisks, GPU gpu, Case house, PSU psu, String username) {
        this.board = board;
        this.cpu = cpu;
        this.rams = rams;
        this.harddisks = harddisks;
        this.gpu = gpu;
        this.house = house;
        this.psu = psu;
        this.like = new HashSet<>();
        this.dislike = new HashSet<>();
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
    public int getLikes() {
        return like.size();
    }
    public int getDislikes() {
        return dislike.size();
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

    //RAM METHODS
    public int getRamSlot() {
        return board.getMemorySlots();
    }
    public int getUsedSlot() {
        int slot = 0;
        for(RAM r : rams)
            slot = slot + r.getQuantity();
        return slot;
    }
    public boolean addRam(RAM ram) {
        if(getRamSlot() - getUsedSlot() < ram.getQuantity())
            return false;
        rams.add(ram);
        return true;
    }

    //HARD DISK METHOD
    public boolean addMemory(Memory memory) {
        return harddisks.add(memory);
    }

    public Motherboard getBoard() {
        return board;
    }
    public void setBoard(Motherboard board) {
        this.board = board;
    }
    public CPU getCpu() {
        return cpu;
    }
    public boolean setCpu(CPU cpu) {
        if(board != null & !board.compatibleCPU(cpu))
            return false;
        this.cpu = cpu;
        return true;
    }
    public List<RAM> getRams() {
        return rams;
    }
    public void setRams(List<RAM> rams) {
        this.rams = rams;
    }
    public List<Memory> getHarddisks() {
        return harddisks;
    }
    public void setHarddisks(List<Memory> harddisks) {
        this.harddisks = harddisks;
    }
    public GPU getGpu() {
        return gpu;
    }
    public void setGpu(GPU gpu) {
        this.gpu = gpu;
    }
    public Case getHouse() {
        return house;
    }
    public boolean setHouse(Case house) {
        if(!board.compatibleCase(house))
            return false;
        this.house = house;
        return true;
    }
    public CPUFan getFan() {
        return fan;
    }
    public void setFan(CPUFan fan) {
        this.fan = fan;
    }
    public PSU getPsu() {
        return psu;
    }
    public void setPsu(PSU psu) {
        this.psu = psu;
    }
    public User getCreator() {
        return creator;
    }
    public void setCreator(User creator) {
        this.creator = creator;
    }
    public Set<String> getLike() {
        return like;
    }
    public void setLike(Set<String> like) {
        this.like = like;
    }
    public Set<String> getDislike() {
        return dislike;
    }
    public void setDislike(Set<String> dislike) {
        this.dislike = dislike;
    }
}
