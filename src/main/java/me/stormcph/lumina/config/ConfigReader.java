package me.stormcph.lumina.config;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.misc.NoTrace;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.*;
import me.stormcph.lumina.utils.misc.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigReader {
    public static void loadConfig(){
        List<Module> modules = ModuleManager.INSTANCE.getModules();
        File config = new File(JsonUtil.configDir+"\\luminaConfig.json");
        if(!config.exists()){
            try{
                config.createNewFile();
            } catch (IOException e) {
                if(NoTrace.shouldLog()) LogUtils.getLogger().error("Unable to create base config file! (IOException)");
            }
            if(NoTrace.shouldLog()) LogUtils.getLogger().warn("Config file not found, creating...");
            ConfigWriter.writeConfig(false, null);
            return;
        }

        boolean compatibilityMode = false; // so if someone attempts to load older configs it doesn't crash

        JsonObject root = new JsonObject();

        for(Module m : modules){
            JsonObject moduleData;
            try {
                moduleData = JsonUtil.getKeyValue(config, m.getName()).getAsJsonObject();
            } catch (Exception e){
                if(NoTrace.shouldLog()) LogUtils.getLogger().error("Unable to get JSON data for module "+m.getName());
                continue; // skip module if not found in config
            }

            if(!compatibilityMode){
                // if compatibility is off,
                try {
                    // new config elements here
                    m.setEnabled(moduleData.get("enabled").getAsJsonPrimitive().getAsBoolean());
                    if(m instanceof HudModule hm){
                        hm.setX(moduleData.get("x").getAsJsonPrimitive().getAsInt());
                        hm.setY(moduleData.get("y").getAsJsonPrimitive().getAsInt());
                        hm.setWidth(moduleData.get("width").getAsJsonPrimitive().getAsInt());
                        hm.setHeight(moduleData.get("height").getAsJsonPrimitive().getAsInt());
                    }
                } catch (Exception e){
                    // DEBUG LOG todo: remove log before release
                    LogUtils.getLogger().error("Exception thrown when attempting to retrieve a config value. Exception: \""+ e +"\", Module name: \""+m.getName()+"\".");
                    LogUtils.getLogger().warn("Enabled compatibility mode");
                    compatibilityMode = true;
                } finally {
                    if(compatibilityMode) {
                        // set the settings to default here
                        m.setEnabled(m.isEnabled()); // not needed, but for an example
                    }
                }
            } else {
                // and also here
                m.setEnabled(m.isEnabled());
            }

            for(Setting s : m.getSettings()){
                if(s instanceof ModeSetting){
                    ((ModeSetting) s).setMode(
                            moduleData.get
                                    (
                                            s.getName()
                                    )
                                    .getAsJsonPrimitive()
                                    .getAsString()
                    );
                } else if (s instanceof BooleanSetting) {
                    ((BooleanSetting) s).setEnabled(
                            moduleData.get(
                                    s.getName()
                            )
                                    .getAsJsonPrimitive()
                                    .getAsBoolean()
                    );
                } else if (s instanceof KeybindSetting) {
                    ((KeybindSetting) s).setKey(
                            moduleData.get(
                                    s.getName()
                            )
                                    .getAsJsonPrimitive()
                                    .getAsInt()
                    );
                } else if (s instanceof NumberSetting) {
                    ((NumberSetting) s).setValue(
                            moduleData.get(
                                            s.getName()
                                    )
                                    .getAsJsonPrimitive()
                                    .getAsInt()
                    );
                } else if (s instanceof TextSetting) {
                    ((TextSetting) s).setText(
                            moduleData.get
                                            (
                                                    s.getName()
                                            )
                                    .getAsJsonPrimitive()
                                    .getAsString()
                    );
                }
                root.add(m.getName(), moduleData);
            }
        }

        if(NoTrace.shouldLog()) LogUtils.getLogger().info("Successfully loaded config!");

    }
}
