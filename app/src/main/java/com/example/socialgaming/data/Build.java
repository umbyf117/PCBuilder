package com.example.socialgaming.data;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.socialgaming.utils.ImageUtils;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
    private UUID uuid;

    private Bitmap image;
    private String name;

    public Build(Motherboard board, CPU cpu, List<RAM> rams, List<Memory> harddisks, GPU gpu, Case house, CPUFan fan, PSU psu, User creator, Set<String> like, Set<String> dislike, UUID uuid, String name, Bitmap uri) {
        setBoard(board);
        setCpu(cpu);
        setRams(rams);
        setHarddisks(harddisks);
        setGpu(gpu);
        setHouse(house);
        setFan(fan);
        setPsu(psu);
        setCreator(creator);
        setLike(like);
        setDislike(dislike);
        setUuid(uuid);
        this.name = name;
        this.image = uri;
    }

    public Build(Motherboard board, CPU cpu, List<RAM> rams, List<Memory> harddisks, GPU gpu, Case house, CPUFan fan, PSU psu, User creator, String name, Bitmap image) {
        this(board, cpu, rams, harddisks, gpu, house, fan, psu, creator, new HashSet<>(), new HashSet<>(), UUID.randomUUID(), name, image);
    }

    public Build() {}

    public void updateWithDocument(DocumentSnapshot documentSnapshot) {
        Map<String, Object> data = documentSnapshot.getData();
        this.board = (Motherboard) data.get("motherboard");
        this.cpu = (CPU) data.get("cpu");
        this.rams = (List<RAM>) data.get("ram");
        this.harddisks = (List<Memory>) data.get("harddisk");
        this.gpu = (GPU) data.get("gpu");
        this.house = (Case) data.get("case");
        this.fan = (CPUFan) data.get("fan");
        this.psu = (PSU) data.get("psu");
        this.creator = (User) data.get("creator");
        this.like = (Set<String>) data.get("like");
        this.dislike = (Set<String>) data.get("dislike");
        this.uuid = (UUID) data.get("uuid");
        this.name = (String) data.get("name");
        this.image = ImageUtils.decodeByteArrayToBitmap((byte[]) data.get("uri"));
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Build)) return false;

        Build build = (Build) o;

        return this.uuid.equals(build.uuid);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    public boolean checkComponents(Build build) {
        if(this == build)
            return true;
        if(!this.board.equals(build.board))
            return false;
        if(!this.cpu.equals(build.cpu))
            return false;
        if(!this.rams.equals(build.rams))
            return false;
        if(!this.gpu.equals(build.gpu))
            return false;
        if(!this.harddisks.equals(build.harddisks))
            return false;
        if(!this.house.equals(build.house))
            return false;
        if(!this.fan.equals(build.fan))
            return false;
        if(!this.psu.equals(build.psu))
            return false;
        return true;
    }

    //GENERATE HASHMAP
    public Map<String, Object> getMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("motherboard", board);
        data.put("cpu", cpu);
        data.put("ram", rams);
        data.put("harddisk", harddisks);
        data.put("gpu", gpu);
        data.put("case", house);
        data.put("fan", fan);
        data.put("psu", psu);
        data.put("creator", creator);
        data.put("like", like);
        data.put("dislike", dislike);
        data.put("uuid", uuid);
        data.put("name", name);
        data.put("uri", image);
        return data;
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

    //VALUES METHODS
    public double getValue() {

        if(like.size() == 0)
            return 0;

        if(dislike.size() == 0)
            return 100.0;

        double value = (like.size() * 10)/(dislike.size() * 10);
        return value/10;
    }

    //GETTER & SETTER
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
    public UUID getUuid() {
        return uuid;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
