package com.example.socialgaming.data;

import java.util.List;

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

    private int like, dislike;

    public Build(Motherboard board, CPU cpu, List<RAM> rams, List<Memory> harddisks, GPU gpu, Case house, PSU psu, User creator) {
        this.board = board;
        this.cpu = cpu;
        this.rams = rams;
        this.harddisks = harddisks;
        this.gpu = gpu;
        this.house = house;
        this.psu = psu;
        this.creator = creator;
    }

    public Build(Motherboard board, CPU cpu, List<RAM> rams, List<Memory> harddisks, GPU gpu, Case house, PSU psu, String username) {
        this.board = board;
        this.cpu = cpu;
        this.rams = rams;
        this.harddisks = harddisks;
        this.gpu = gpu;
        this.house = house;
        this.psu = psu;

    }
}
