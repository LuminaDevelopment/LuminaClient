package me.stormcph.lumina.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
            File myObj = new File("filename.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                builder.append(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
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
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
