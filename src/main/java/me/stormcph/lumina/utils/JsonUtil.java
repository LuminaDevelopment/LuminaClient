package me.stormcph.lumina.utils;

import com.google.gson.JsonObject;
import me.stormcph.lumina.Lumina;
import me.stormcph.lumina.module.impl.misc.NoTrace;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonUtil {
    public static final Path configDir = FabricLoader.getInstance().getConfigDir();

    public static void writeJson(Path file, JsonObject data){
        writeFile(file, data.toString());
    }

    private static void writeFile(Path file, String data) {
        try (var writer = Files.newBufferedWriter(file)) {
            writer.write(data);
        } catch (IOException e) {
            if(NoTrace.shouldLog()) Lumina.getInstance().logger.error(e.toString());
        }
    }
}
