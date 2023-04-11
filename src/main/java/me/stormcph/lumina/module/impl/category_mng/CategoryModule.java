package me.stormcph.lumina.module.impl.category_mng;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;

public class CategoryModule extends Module {

    public CategoryModule(String name) {
        super(name, "Enable or disable", Category.CATEGORY_MNG);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        for(Category c : Category.values()){
            if(c.name.equals(this.getName())) {
                c.setHidden(false);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for(Category c : Category.values()) {
            if(c.name.equals(this.getName())) {
                c.setHidden(true);
            }
        }
    }
}
