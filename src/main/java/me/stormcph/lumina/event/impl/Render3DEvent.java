package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent extends Event {

    private final MatrixStack matrices;
    public Render3DEvent(MatrixStack matrices) {
        this.matrices = matrices;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }
}
