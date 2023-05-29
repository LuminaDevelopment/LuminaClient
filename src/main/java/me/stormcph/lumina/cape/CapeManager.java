package me.stormcph.lumina.cape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CapeManager {

    public static final List<Cape> capes = new ArrayList<>();
    public static HashMap<String, Cape> players = new HashMap<>();

    public static void init() {
        // the player capes are stored here, string as the name and the name of the file
        addCape(new Cape("TestCape", "lumina-b.png"){});
        addCape(new Cape("TestCape2", "lumina-w.png"){});
        addCape(new Cape("Master", "master.png"){});
    }

    private static void addCape(Cape cape) {
        capes.add(cape);
    }

    public static Cape getCape(String name) {
        for (Cape cape : capes) {
            if (cape.getName().equalsIgnoreCase(name)) {
                return cape;
            }
        }
        return null;
    }
}
