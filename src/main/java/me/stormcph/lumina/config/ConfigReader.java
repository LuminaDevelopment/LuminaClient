package me.stormcph.lumina.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.*;
import me.stormcph.lumina.utils.JsonUtil;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ConfigReader {
    public static void loadConfig(){
        List<Module> modules = ModuleManager.INSTANCE.getModules();
        File config = new File(JsonUtil.configDir+"\\luminaConfig.json");
        if(!config.exists()){
            LogUtils.getLogger().warn("Config file not found, creating...");
            ConfigWriter.writeConfig();
            return;
        }
        for(Module m : modules){
            JsonObject moduleData = JsonUtil.getKeyValue(config, m.getName()).getAsJsonObject();
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
            }
        }
    }
}
