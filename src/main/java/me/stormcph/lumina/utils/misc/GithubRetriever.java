package me.stormcph.lumina.utils.misc;

import me.stormcph.lumina.cape.Cape;
import me.stormcph.lumina.cape.CapeManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GithubRetriever {

    private final String link = "https://raw.githubusercontent.com/CorruptionHades/LuminaCapes/main/data.txt";

    public void retrieve() {
        try {
            HashMap<String, String> playerData = new HashMap<>();
            // Create a URL object with the specified URL
            URL url = new URL(link);
            Encryption encryption = new Encryption();

            // Open a connection to the URL
            URLConnection connection = url.openConnection();

            // Create a BufferedReader to read the contents of the webpage
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            List<String> retrieved = new ArrayList<>();
            String line;
            // Read each line from the BufferedReader and append it
            while ((line = reader.readLine()) != null) {
                retrieved.add(line);
            }
            // Close the reader
            reader.close();
            connection.getInputStream().close();

            for (String datum : retrieved) {
                String[] playerDatas = datum.split("\\[");
                String name = encryption.decrypt(playerDatas[0]);
                String capeName = encryption.decrypt(playerDatas[1]);
                playerData.put(name, capeName);
            }

            for (String s : playerData.keySet()) {
                Cape cape = CapeManager.getCape(playerData.get(s));
                CapeManager.players.put(s, cape);
            }

            playerData.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void retrieveCustomData() {
        try {
            Encryption encryption = new Encryption();
            String link = "https://raw.githubusercontent.com/CorruptionHades/LuminaCapes/main/customData.txt";
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            List<String> retrieved = new ArrayList<>();
            String line;
            // Read each line from the BufferedReader and append it
            while ((line = reader.readLine()) != null) {
                retrieved.add(line);
            }
            reader.close();
            connection.getInputStream().close();

            int i = 0;
            for (String datum : retrieved) {
                String[] data = datum.split("#");
                String capeName = data[0];
                String textureString = data[1];
                String[] textures = textureString.split(":");

                Cape cape = Cape.animatedFromBase64(capeName, textures, "png", i);
                CapeManager.capes.add(cape);
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
