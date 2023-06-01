package me.stormcph.lumina.cape;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Cape {

    private final String name;
    private final List<Identifier> textures;
    private int index;
    private int currentTick, framePerTick;

    public Cape(String name, int framePerTick, String...fileNames) {
        this.name = name;
        this.framePerTick = framePerTick;
        this.currentTick = 0;
        this.textures = new ArrayList<>();
        for (String s : fileNames) {
            this.textures.add(new Identifier("lumina", "textures/cape/" + s));
        }
        this.index = 0;
    }

    public static Cape animatedFromFolder(String name, String folder, int frameAmount, String extensionName) {

        List<String> textures = new ArrayList<>();

        for(int i = 0; i < frameAmount; i++) {
            textures.add(folder + "/" + i + "." + extensionName);
        }

        String[] array = textures.toArray(new String[0]);
        return new Cape(name, 1, array) {};
    }

    public void setFramePerTick(int framePerTick) {
        this.framePerTick = framePerTick;
    }

    public Identifier getTexture() {
        return textures.get(index);
    }

    public void update() {
        if(currentTick > framePerTick) {
            currentTick = 0;
            index++;
            if(index > textures.size() - 1) {
                index = 0;
            }
        }
        currentTick++;
    }

    public String getName() {
        return name;
    }
}
