package me.stormcph.lumina.config;

import com.google.gson.JsonObject;
import me.stormcph.lumina.Lumina;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.misc.NoTrace;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.*;
import me.stormcph.lumina.utils.JsonUtil;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class ConfigWriter {

    public static void writeConfig(boolean compatibilityMode, @Nullable JsonObject presetData){
        Path config = JsonUtil.configDir.resolve("luminaConfig.json");

        JsonObject config_obj = new JsonObject();

        for(Module m : ModuleManager.INSTANCE.getModules()){
            JsonObject module = new JsonObject();
            if(compatibilityMode){
                module = presetData;
            } else {
                for(Setting s : m.getSettings()){
                    if(s instanceof ModeSetting){
                        module.addProperty(s.getName(), ((ModeSetting) s).getMode());
                    } else if (s instanceof BooleanSetting) {
                        module.addProperty(s.getName(), ((BooleanSetting) s).isEnabled());
                    } else if (s instanceof KeybindSetting) {
                        module.addProperty(s.getName(), ((KeybindSetting) s).getKey());
                    } else if (s instanceof NumberSetting) {
                        module.addProperty(s.getName(), ((NumberSetting) s).getValue());
                    } else if (s instanceof TextSetting) {
                        module.addProperty(s.getName(), ((TextSetting) s).getText());
                    }
                }

            }

            // new config elements here

            module.addProperty("enabled", m.isEnabled());

            // end

            config_obj.add(m.getName(), module);
        }

        try {
            JsonUtil.writeJson(config, config_obj);
            if(NoTrace.shouldLog()) Lumina.getInstance().logger.info("Successfully saved config");
        } catch (Exception e){
            if(NoTrace.shouldLog()) Lumina.getInstance().logger.error("Unable to write config!", e);
        }

    }

}
