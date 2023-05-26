package me.stormcph.lumina.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.stormcph.lumina.Lumina;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.misc.NoTrace;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.*;
import me.stormcph.lumina.utils.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ConfigReader {
    public static void loadConfig(){
        List<Module> modules = ModuleManager.INSTANCE.getModules();
        Path config = JsonUtil.configDir.resolve("luminaConfig.json");
        if (!Files.exists(config)) {
            try{
                Files.createFile(config);
            } catch (IOException e) {
                if(NoTrace.shouldLog()) Lumina.getInstance().logger.error("Unable to create base config file! (IOException)");
            }
            if(NoTrace.shouldLog()) Lumina.getInstance().logger.warn("Config file not found, creating...");
            ConfigWriter.writeConfig(false, null);
            return;
        }

        boolean compatibilityMode = false; // so if someone attempts to load older configs it doesn't crash

        try (var fileReader = Files.newBufferedReader(config)) { // Only parse the config file once. -C
            JsonObject configJson = JsonParser.parseReader(fileReader).getAsJsonObject();
            JsonObject root = new JsonObject();

            for(Module m : modules){
                JsonObject moduleData = configJson.getAsJsonObject(m.getName());

                if(!compatibilityMode){
                    // if compatibility is off,
                    try {
                        // new config elements here
                        m.setEnabled(moduleData.get("enabled").getAsJsonPrimitive().getAsBoolean());
                    } catch (Exception e){
                        // DEBUG LOG todo: remove
                        Lumina.getInstance().logger.error("Exception thrown when attempting to retrieve \"enabled\" on module \"%s\".".formatted(m.getName()), e);
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
        } catch (IOException e) {
            if (NoTrace.shouldLog()) Lumina.getInstance().logger.error("An error occured.", e);
        }

        if(NoTrace.shouldLog()) Lumina.getInstance().logger.info("Successfully loaded config!");
    }
}
