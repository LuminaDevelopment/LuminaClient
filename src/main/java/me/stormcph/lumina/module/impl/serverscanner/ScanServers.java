package me.stormcph.lumina.module.impl.serverscanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.stormcph.lumina.mixins.MultiplayerScreenAccessor;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.setting.impl.TextSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class ScanServers extends Module {
    private MinecraftClient mc = MinecraftClient.getInstance();
    public NumberSetting scanCount = new NumberSetting("Scan Count", 1, 100, 1, 1);
    public TextSetting defaultName = new TextSetting("Default Name", "Scanned Server");
    public BooleanSetting autoDelete = new BooleanSetting("Auto Delete", true);

    public ScanServers() {
        super("Scan", "Find 100 servers", Category.SERVER_SCANNER);
        addSettings(scanCount, defaultName);
        removeKeybind();
    }

    @Override
    public void onEnable() {
        super.toggle();
        ServerList serverList = ((MultiplayerScreen) mc.currentScreen).getServerList();
        // remove previous scans
        if (autoDelete.isEnabled()) {
            for (int i = serverList.size() - 1; i >= 0; i--) {
                if (serverList.get(i).name.equals(defaultName.getText())) {
                    serverList.remove(serverList.get(i));
                }
            }
            serverList.saveFile();
        }

        Document query = new Document();
        Module MinOnline = ModuleManager.INSTANCE.getModuleByName("MinOnline");
        Module MaxOnline = ModuleManager.INSTANCE.getModuleByName("MaxOnline");
        Module PlayerCap = ModuleManager.INSTANCE.getModuleByName("PlayerCap");
        Module IsFull = ModuleManager.INSTANCE.getModuleByName("IsFull");
        Module Version = ModuleManager.INSTANCE.getModuleByName("Version");
        Module HasImage = ModuleManager.INSTANCE.getModuleByName("HasImage");
        Module Description = ModuleManager.INSTANCE.getModuleByName("Description");
        Module Player = ModuleManager.INSTANCE.getModuleByName("Player");
        Module SeenAfter = ModuleManager.INSTANCE.getModuleByName("SeenAfter");
        Module IPRange = ModuleManager.INSTANCE.getModuleByName("IPRange");

        if (MinOnline.isEnabled() || MaxOnline.isEnabled()) {
            Document playersOnlineDoc = new Document();
            if (MinOnline.isEnabled()) {
                Integer minOnline = null;
                try {
                    minOnline = Integer.parseInt(((TextSetting) MinOnline.getSettings().get(0)).getText());
                } catch (Exception e) {
                }
                if (minOnline != null) playersOnlineDoc.append("$gte", minOnline);
            }
            if (MaxOnline.isEnabled()) {
                Integer maxOnline = null;
                try {
                    maxOnline = Integer.parseInt(((TextSetting) MaxOnline.getSettings().get(0)).getText());
                } catch (Exception e) {}
                if (maxOnline != null) playersOnlineDoc.append("$lte", maxOnline);
            }
            query.append("players.online", playersOnlineDoc);
        }

        if (PlayerCap.isEnabled()) {
            Integer playerCap = null;
            try {
                playerCap = Integer.parseInt(((TextSetting) MaxOnline.getSettings().get(0)).getText());
            } catch (Exception e) {}
            if (playerCap != null) query.append("players.max", playerCap);
        }
        if (IsFull.isEnabled()) {
            if (((BooleanSetting)IsFull.getSettings().get(0)).isEnabled()) {
                query.append("$expr", new Document("$eq", Arrays.asList("$players.online", "$players.max")));
            } else {
                query.append("$expr", new Document("$ne", Arrays.asList("players.online", "$players.max")));
            }
            query.append("players", new Document("$ne", null));
        }
        if (Version.isEnabled()) query.append("version.name", new Document("$regex", ((TextSetting)Version.getSettings().get(0)).getText()));
        if (HasImage.isEnabled()) query.append("hasFavicon", ((BooleanSetting)HasImage.getSettings().get(0)).isEnabled());
        if (Description.isEnabled()) query.append("$or", Arrays.asList(
                new Document().append("description", new Document().append("$regex", ((TextSetting)Description.getSettings().get(0)).getText()).append("$options", "i")),
                new Document().append("description.text", new Document().append("$regex", ((TextSetting)Description.getSettings().get(0)).getText()).append("$options", "i")),
                new Document().append("description.extra", new Document().append("$elemMatch", new Document().append("text", new Document("$regex", ((TextSetting)Description.getSettings().get(0)).getText()).append("$options", "i"))))
                ));
        if (Player.isEnabled()) {
            if (query.get("players") == null) query.append("players", new Document("$ne", null));
            query.append("players.sample", new Document("$exists", true).append("$elemMatch", new Document("name", ((TextSetting)Player.getSettings().get(0)).getText())));
        }
        if (SeenAfter.isEnabled()) {
            Integer seenAfter = null;
            try {
                seenAfter = Integer.parseInt(((TextSetting)SeenAfter.getSettings().get(0)).getText());
            } catch (Exception e) {}
            if (seenAfter != null) query.append("lastSeen", new Document("$gte", seenAfter));
        }
        if (IPRange.isEnabled()) {
            String ip = ((TextSetting)IPRange.getSettings().get(0)).getText().split("/")[0];
            int range = Integer.parseInt(((TextSetting)IPRange.getSettings().get(0)).getText().split("/")[1]);
            int ipCount = (int)Math.pow(2, 32 - range);
            String[] octets = ip.split("\\.");
            for (var i = 0; i < octets.length; i++) {
                if (Math.pow(256, i) < ipCount) {
                    int min = Integer.parseInt(octets[octets.length - i - 1]);
                    int max = 255;
                    if (Math.pow(256, i + 1) < ipCount) {
                        min = 0;
                    } else {
                        max = ipCount / 256;
                    }
                    octets[octets.length - i - 1] = "(" + min + "|[1-9]\\d{0,2}|[1-9]\\d{0,1}\\d|" + max + ")";
                }
            }

            query.append("ip", new Document("$regex", "^" + octets[0] + "." + octets[1] + "." + octets[2] + "." + octets[3] + "$").append("$options", "i"));
        }

        int skip = 0;
        Module Page = ModuleManager.INSTANCE.getModuleByName("Page");
        if (Page.isEnabled()) skip = Integer.parseInt(((TextSetting)Page.getSettings().get(0)).getText()) * scanCount.getIntValue();
        try {
            URL apiUrl = new URL("https://api.cornbread2100.com/servers?limit=" + scanCount.getIntValue() + "&skip=" + skip);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Set the necessary HTTP Method and Headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(query.toJson().toString().getBytes().length));

            connection.setDoOutput(true);

            // Write the request body to the connection's output stream
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(query.toJson().toString());
            outputStream.flush();
            outputStream.close();

            // Read the response from the server
            int responseCode = connection.getResponseCode();
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Print the response
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + response);
            if (responseCode == 200) {
                ArrayList<Document> result = (new Gson()).fromJson(response.toString(), new TypeToken<ArrayList<Document>>(){}.getType());
                System.out.println(result);
                System.out.println(result.get(0));
                System.out.println(result.get(0).get("ip"));
                result.forEach((doc -> {
                    serverList.add(new ServerInfo(defaultName.getText(), doc.get("ip") + ":" + doc.get("port").toString().substring(0, doc.get("port").toString().indexOf(".")), false), false);
                }));
            }

            connection.disconnect();
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
//      scannedServers.find(query).skip(skip).limit(scanCount.getIntValue()).forEach((doc -> {
//          serverList.add(new ServerInfo(defaultName.getText(), doc.get("ip") + ":" + doc.get("port"), false), false);
//      }));
        serverList.saveFile();
        mc.setScreen(new MultiplayerScreen(((MultiplayerScreenAccessor)mc.currentScreen).getParent()));
    }
}
