package me.stormcph.lumina.module;

import me.stormcph.lumina.module.impl.ghost.*;
import me.stormcph.lumina.module.impl.category_mng.impl.*;
import me.stormcph.lumina.module.impl.combat.*;
import me.stormcph.lumina.module.impl.misc.*;
import me.stormcph.lumina.module.impl.movement.*;
import me.stormcph.lumina.module.impl.movement.scaffold.*;
import me.stormcph.lumina.module.impl.player.*;
import me.stormcph.lumina.module.impl.render.*;
import me.stormcph.lumina.setting.impl.KeybindSetting;

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
        add(new Flight()); //blatant
        add(new Sprint()); //blatant - for now
        add(new FakeLag()); //blatant
        add(new Speed()); //blatant
        add(new Scaffold()); //blatant

        // Combat
        add(new Killaura()); //blatant
        add(new PvpHubExploit()); //bypass
        add(new TriggerBot()); //bypass

        // Misc
        add(new PacketLogger());
        add(new ChatHandler());
        add(new NameProtect());
        add(new Advertise());
        add(new NoTrace());

        // Player
        add(new ChestStealer());
        add(new NoFall());

        // Ghost
        add(new CrystalPop());
        add(new AutoDoubleHand());
        add(new CrystalPlacer());
        add(new SCC());
        add(new CrystalPopAndPlace());
        add(new PearlMacro());

        // Render
        add(new Arraylist());
        add(new Animations());
        add(new BetterHome());
        add(new EveryoneWantsThis());
        add(new LuminaLogo());
        add(new ClickguiModule());
        add(new Cape());
        add(new ESP());
        add(new TargetHUD());
        add(new NoHurtCam());
        add(new ViewModel());

        // Hide / show categories
        add(new CombatCata());
        add(new GhostCategory());
        add(new MiscCategory());
        add(new MovementCategory());
        add(new PlayerCategory());
        add(new RenderCategory());

        for (Module module : modules) {
            module.addSettings(new KeybindSetting("Keybind", module.getKey(), module));
        }
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
