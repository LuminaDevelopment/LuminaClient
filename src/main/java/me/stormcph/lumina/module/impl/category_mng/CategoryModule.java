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
        for(Category c : categories){
            if(c.name==this.getName()){
                c.isHidden = false;
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for(Category c : categories){
            if(c.name==this.getName()){
                c.isHidden = true;
            }
        }
    }
}
