package me.stormcph.lumina.module.impl.category_mng;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.utils.ChatUtils;

public class CategoryModule extends Module {
    private Category detectedCategory;
    public CategoryModule(String name) {
        super(name, "Enable or disable", Category.CATEGORY_MNG);
        this.detectedCategory = null;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        for(Category c : Category.values()){
            if(c.name==this.getName()){
                c.setHidden(false);
                ChatUtils.sendMsg("CATEGORY FOUND!!! name: "+c.name+", isHidden: "+c.isHidden);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for(Category c : Category.values()) {
            if(c.name==this.getName()){
                c.setHidden(true);
            }
        }
    }

//    @EventTarget
//    public void onUpdate(EventUpdate e){
//        ChatUtils.sendMsg("[CAT-"+this.getName()+"] ");
//    }
}
