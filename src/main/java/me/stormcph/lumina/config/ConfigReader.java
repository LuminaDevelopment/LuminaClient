package me.stormcph.lumina.config;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
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

        for(Module m : modules) {
            if (!m.savesSettings()) continue;
            JsonObject moduleData = JsonUtil.getKeyValue(config, m.getName()).getAsJsonObject();

            if(!compatibilityMode){
                // if compatibility is off,
                try {
                    // new config elements here
                    m.setEnabled(moduleData.get("enabled").getAsJsonPrimitive().getAsBoolean());
                } catch (Exception e){
                    // DEBUG LOG todo: remove
                    LogUtils.getLogger().error("Exception thrown when attempting to retrieve \"enabled\". E=\""+ e +"\", M.name=\""+m.getName()+"\"");
                    compatibilityMode = true;
                }
            } else {
                // set the stuff to default here
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
