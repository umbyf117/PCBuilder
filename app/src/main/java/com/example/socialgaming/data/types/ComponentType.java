package com.example.socialgaming.data.types;

import com.example.socialgaming.data.CPU;
import com.example.socialgaming.data.CPUFan;
import com.example.socialgaming.data.Case;
import com.example.socialgaming.data.GPU;
import com.example.socialgaming.data.Memory;
import com.example.socialgaming.data.Motherboard;
import com.example.socialgaming.data.PSU;
import com.example.socialgaming.data.RAM;

import java.io.Serializable;
import java.util.Locale;

public enum ComponentType implements Serializable {
    CASE,
    CPU,
    CPU_FAN,
    GPU,
    MEMORY,
    MOTHERBOARD,
    PSU,
    RAM;

    public String toCapitalCase() {
        return toString().charAt(0) + toString().toLowerCase().substring(1);
    }

    public int toInt() {
        switch(this) {
            case CASE:
                return 0;
            case CPU:
                return 1;
            case CPU_FAN:
                return 2;
            case GPU:
                return 3;
            case MEMORY:
                return 4;
            case MOTHERBOARD:
                return 5;
            case PSU:
                return 6;
            case RAM:
                return 7;
        }
        return -1;
    }

    public static ComponentType getComponentTypeByInt(int number) {
        switch(number) {
            case 0:
                return CASE;
            case 1:
                return CPU;
            case 2:
                return CPU_FAN;
            case 3:
                return GPU;
            case 4:
                return MEMORY;
            case 5:
                return MOTHERBOARD;
            case 6:
                return PSU;
            case 7:
                return RAM;
        }
        return null;
    }

}

