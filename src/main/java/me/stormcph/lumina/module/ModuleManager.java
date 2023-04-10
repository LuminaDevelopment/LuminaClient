package me.stormcph.lumina.module;

//import me.stormcph.lumina.module.impl.ghost.*;
import me.stormcph.lumina.module.impl.category_mng.impl.*;
import me.stormcph.lumina.module.impl.movement.*;
import me.stormcph.lumina.module.impl.render.*;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public static final ModuleManager INSTANCE = new ModuleManager();

    public ModuleManager() {
        init();
    }

    public void init() {
        // Movement
        add(new Flight());
        add(new Sprint());

        // GhostCategory

        // Render
        add(new Arraylist());

        // Hide / show categories
        add(new CombatCata());
        add(new GhostCategory());
        add(new MiscCategory());
        add(new MovementCategory());
        add(new PlayerCategory());
        add(new RenderCategory());
    }

    public void add(Module m) {
        modules.add(m);
    }

    public void remove(Module m) {
        modules.remove(m);
    }

    public List<Module> getModules() {
        return modules;
    }

    public Module getModuleByName(String name){
        for(Module module : modules) {
            if(module.getName().equalsIgnoreCase(name)) return module;
        }

        return null;
    }

    public ArrayList<Module> getModulesByCategory(Category category) {
        ArrayList<Module> modules = new ArrayList<>();
        for(Module m : this.modules){
            if(m.getCategory().equals(category)){
                modules.add(m);
            }
        }
        return modules;
    }

    public Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : modules) {
            if (m.getClass() != cls) {
                continue;
            }
            return m;
        }
        return null;
    }

    public List<Module> getEnabledModules() {
        List<Module> enabled = new ArrayList<>();
        for(Module m : getModules()) {
            if(m.isEnabled()) {
                enabled.add(m);
            }
        }

        return enabled;
    }
}
