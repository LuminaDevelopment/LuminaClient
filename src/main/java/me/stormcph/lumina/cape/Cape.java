package me.stormcph.lumina.cape;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.DynamicTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
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

    public Cape(String name, int framePerTick, Identifier...identifiers) {
        this.name = name;
        this.framePerTick = framePerTick;
        this.currentTick = 0;
        this.textures = new ArrayList<>();
        Collections.addAll(textures, identifiers);
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

    public static Cape staticFromBase64(String name, String base64, String format, int customId) {
        try {
            Identifier identifier = new Identifier("your_mod_id", "custom_texture" + customId);
            saveBufferedImageAsIdentifier(base64, identifier, format);
            Cape cape = new Cape(name, 1, identifier);
            CapeManager.capes.add(cape);
            return cape;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Cape animatedFromBase64(String capeName, String[] base64, String format, int customId) {
        try {
            List<Identifier> identifiers = new ArrayList<>();
            int m = 0;
            for (String s : base64) {
                Identifier identifier = new Identifier("lumina", "custom_texture" + m + "" + customId);
                saveBufferedImageAsIdentifier(s, identifier, format);
                identifiers.add(identifier);
                m++;
            }
            Identifier[] array = identifiers.toArray(new Identifier[0]);
            return new Cape(capeName, 1, array);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void saveBufferedImageAsIdentifier(String base64Texture, Identifier identifier, String format) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64Texture)));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, stream);
        byte[] bytes = stream.toByteArray();

        ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
        data.flip();
        NativeImage img = NativeImage.read(data);
        NativeImageBackedTexture texture = new NativeImageBackedTexture(img);

        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, texture));
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
