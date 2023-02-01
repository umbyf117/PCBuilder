package com.example.socialgaming.data.types;

import java.util.Locale;

public enum ComponentType {
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

}

