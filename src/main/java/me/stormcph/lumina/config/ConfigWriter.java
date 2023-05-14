package me.stormcph.lumina.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.*;
import me.stormcph.lumina.utils.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigWriter {

    public static void writeConfig(){

        File config = new File(JsonUtil.configDir+"\\luminaConfig.json");

        JsonObject config_obj = new JsonObject();

        for(Module m : ModuleManager.INSTANCE.getModules()){
            JsonObject module = new JsonObject();
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
            config_obj.add(m.getName(), module);
        }

        try {
            JsonUtil.writeJson(config, config_obj);
        } catch (Exception e){
            LogUtils.getLogger().error("Unable to write config!!");
        }

    }

}
