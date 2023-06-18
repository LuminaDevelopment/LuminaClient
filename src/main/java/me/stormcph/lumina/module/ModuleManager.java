package me.stormcph.lumina.module;

import me.stormcph.lumina.module.impl.ghost.*;
import me.stormcph.lumina.module.impl.category_mng.impl.*;
import me.stormcph.lumina.module.impl.combat.*;
import me.stormcph.lumina.module.impl.misc.*;
import me.stormcph.lumina.module.impl.movement.*;
import me.stormcph.lumina.module.impl.movement.scaffold.*;
import me.stormcph.lumina.module.impl.player.*;
import me.stormcph.lumina.module.impl.render.*;
import me.stormcph.lumina.module.impl.serverscanner.*;
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
        add(new NoFall()); //blatant

        // Combat
        add(new Killaura()); //blatant
        add(new Criticals()); //blatant
        add(new PvpHubExploit()); //bypass
        add(new TriggerBot()); // (pretty blatant cause no item based cooldown)
        add(new AutoTotem()); //blatant


        // Misc
        add(new PacketLogger());
        add(new ChatHandler());
//        add(new ItemDump()); not finished yet
        add(new NameProtect());
        add(new Advertise());
//        add(new AutoEz()); it's not finished yet cause i havent implemented a way to detect that it was actually you that killed the entity
        add(new NoTrace());
        add(new Freecam());

        // Player
        add(new AutoArmor()); //blatant
        add(new ChestStealer()); //blatant

        // Ghost
        add(new CrystalPop());
        add(new AutoDoubleHand());
        add(new CrystalPlacer());
        add(new SCC());
//        add(new LegitTotem());
        add(new CrystalPopAndPlace());
        add(new PearlMacro());
        add(new AnchorMacro());
        add(new AutoRefill());

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
        add(new Notifications());
        add(new Nametags());
        add(new FullBright());
        add(new XRay());

        // Server Scanner
        add(new IPRangeFilter());
        add(new SeenAfterFilter());
        add(new PlayerFilter());
        add(new DescriptionFilter());
        add(new HasImageFilter());
        add(new VersionFilter());
        add(new IsFullFilter());
        add(new PlayerCapFilter());
        add(new MaxOnlineFilter());
        add(new MinOnlineFilter());
        add(new ScanPage());
        add(new ScanServers());

        // Hide / show categories
        add(new CombatCata());
        add(new GhostCategory());
        add(new MiscCategory());
        add(new MovementCategory());
        add(new PlayerCategory());
        add(new RenderCategory());

        for (Module module : modules) {
            if (module.hasKeybind()) module.addSettings(new KeybindSetting("Keybind", module.getKey(), module));
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

    /**
     * @deprecated Use getModuleByClass for getting a module, should only be used for the config/bind command
     * @param name The Name of the module
     */
    @Deprecated
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
