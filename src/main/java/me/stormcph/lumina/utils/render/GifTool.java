package me.stormcph.lumina.utils.render;

import net.minecraft.util.Identifier;

public class GifTool {

    public static Identifier[] getFrames(String path, int frames) {
        Identifier[] identifiers = new Identifier[frames];
        for (int i = 1; i < frames; i++) {
            identifiers[i] = new Identifier("lumina", path + i + ".png");
        }
        return identifiers;
    }
}
