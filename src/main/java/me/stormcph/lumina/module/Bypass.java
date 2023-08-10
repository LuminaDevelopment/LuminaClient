package me.stormcph.lumina.module;

import java.util.ArrayList;
import java.util.List;

public enum Bypass {

    BLATANT("Blatant"), SEMI("Semi"),/* BYPASS("Bypass"), */GHOST("Ghost"), OTHER("Other"), ALL("All"); // i deleted the bypass option cayse we werent really using it and it was making it way too clutered

    private final String name;

    Bypass(String name) {
        this.name = name;
    }

    public static List<String> getValues() {
        List<String> values = new ArrayList<>();
        for (Bypass value : Bypass.values()) {
            values.add(value.name);
        }
        values.add("All");
        return values;
    }

    public String getName() {
        return name;
    }
}
