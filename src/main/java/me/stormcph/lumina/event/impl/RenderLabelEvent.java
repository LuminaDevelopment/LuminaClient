package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

public class RenderLabelEvent extends Event {

    private final Entity entity;
    private Text text;
    private final MatrixStack matrices;

    public RenderLabelEvent(Entity entity, Text text, MatrixStack matrices) {
        this.entity = entity;
        this.text = text;
        this.matrices = matrices;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public Entity getEntity() {
        return entity;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
