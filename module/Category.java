package me.stormcph.lumina.module;

import java.util.ArrayList;
import java.util.List;

import static me.stormcph.lumina.module.Module.categories;

public enum Category {
    COMBAT("Combat"),
    GHOST("Ghost"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    MISC("Misc"),
    CATEGORY_MNG("Category");

    public String name;
    public boolean isHidden;


    Category(String name) {
        this.name = name;
        this.isHidden = true;
        categories.add(this);
        if(this.name=="Category") setHidden(false);
    }

    public void setHidden(boolean isHidden){
        this.isHidden = isHidden;
    }
}
