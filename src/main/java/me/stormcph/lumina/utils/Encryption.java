package me.stormcph.lumina.utils;

import net.minecraft.client.MinecraftClient;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Encryption {

    private final List<Character> list;
    private final List<Character> shuffledList;
    private char character;
    private char[] letters;

    public Encryption() {
        list = new ArrayList<>();
        shuffledList = new ArrayList<>();
        character = ' ';

        newKey();
    }

    public void newKey() {
        character = ' ';
        list.clear();
        shuffledList.clear();

        for(int i = 32; i < 127; i++) {
            list.add(character);
            character++;
        }
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec("uefgniowqhfiodhg".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decodedBytes = Base64.getDecoder().decode("wCLNfJIqrZZe44SN0frX52q29EAheaELKR3eHys8FhsZocsKDAOpdrMk3jnCsdl90PbslC/8J45weNmPaWuOQLcQnUn9kStYV2DO6lDA+6vXYSJn4cdXMKtzJbfuO1Aq");
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            String text = new String(decryptedBytes, StandardCharsets.UTF_8);
            for(char c : text.toCharArray()) {
                shuffledList.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String decrypt(String text) {
        letters = text.toCharArray();

        for(int i = 0; i < letters.length; i++) {
            for(int j = 0; j < shuffledList.size(); j++) {
                if(letters[i] == shuffledList.get(j)) {
                    letters[i] = list.get(j);
                    break;
                }
            }
        }

        return new String(letters);
    }
}
