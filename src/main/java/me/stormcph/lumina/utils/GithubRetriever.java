package me.stormcph.lumina.utils;

import com.mojang.logging.LogUtils;
import me.stormcph.lumina.cape.Cape;
import me.stormcph.lumina.cape.CapeManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Scanner;

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

            String retrieved = "";

            String line;
            // Read each line from the BufferedReader and append it to the content StringBuilder
            while ((line = reader.readLine()) != null) {
                retrieved += line + "\n";
            }

            // Close the reader
            reader.close();
            connection.getInputStream().close();

            String[] data = retrieved.split("\n");
            for (String datum : data) {
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

    public static void main(String[] args) {
        CapeManager.init();
        new GithubRetriever().retrieve();
        CapeManager.players.forEach((s, cape) -> System.out.println(s + " " + cape.getName()));

        System.out.println("Please enter the name to add: ");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        System.out.println("Please enter the cape name to add: ");
        String capeName = sc.nextLine();

        String encrypted = new Encryption().encrypt(name + "|" + capeName);
        System.out.println(encrypted);
    }
}
