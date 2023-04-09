package me.stormcph.lumina.module;

public enum Category {
    COMBAT("Combat"),
    GHOST("Ghost"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    MISC("Misc");

    public String name;

    Category(String name) {
        this.name = name;
    }
}
