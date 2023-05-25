package me.stormcph.lumina.utils;

import java.util.ArrayList;
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

        String chars = "CS2~xQnUh'#0*/,T1P=&:u$Aw|^(LkyRjr;%}o)!Ya6eJNfb4{GdEO .WFtZM]\\>XcsK`p5?iB_I<-qVmg3D8+z9@\"lH[7v";

        for(char c : chars.toCharArray()) {
            shuffledList.add(c);
        }
    }

    public String encrypt(String text) {
        letters = text.toCharArray();

        for(int i = 0; i < letters.length; i++) {
            for(int j = 0; j < list.size(); j++) {
                if(letters[i] == list.get(j)) {
                    letters[i] = shuffledList.get(j);
                    break;
                }
            }
        }

         return new String(letters);
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
