package me.stormcph.lumina.utils.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import me.stormcph.lumina.Lumina;
import me.stormcph.lumina.module.impl.misc.NoTrace;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class JsonUtil {
    public static String configDir = FabricLoader.getInstance().getConfigDir().toString();

    public static JsonElement getKeyValue(File file, String key) {
        return JsonParser.parseString(readFile(file)).getAsJsonObject().get(key);
    }

    private static String readFile(File file) {
        StringBuilder builder = new StringBuilder();
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                builder.append(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            Lumina.getInstance().getLogger().error("An error occurred.");
            Lumina.getInstance().getLogger().error(e.getMessage());
        }
        return builder.toString();
    }

    public static void writeJson(File file, JsonObject data){
        writeFile(file, data.toString());
    }

    private static void writeFile(File file, String data) {
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            if(NoTrace.shouldLog()) LogUtils.getLogger().error(e.toString());
        }finally{
            try {
                fr.close();
            } catch (IOException e) {
                if(NoTrace.shouldLog()) LogUtils.getLogger().error(e.toString());
            }
        }
    }

}
