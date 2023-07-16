package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.render.ColorUtil;
import me.stormcph.lumina.utils.animations.Pair;
import me.stormcph.lumina.utils.render.RenderUtils;
import me.stormcph.lumina.utils.animations.Animation;
import me.stormcph.lumina.utils.animations.Direction;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class Arraylist extends HudModule {

    private final NumberSetting colorSpeed = new NumberSetting("Color Speed", 2, 30, 15, 1);
    private final NumberSetting colorSpread = new NumberSetting("Color Spread", 5, 100, 20, 1);

    public Arraylist() {
        super("Arraylist", "Show enabled mods", Category.RENDER, 10, 10, 10, 10);
        addSettings(colorSpeed, colorSpread);
    }

    public List<Module> modules;

    public void getModulesAndSort() {
        modules = ModuleManager.INSTANCE.getModules();
        boolean topLeft = getX() < (mc.getWindow().getScaledWidth() / 2) && getY() < (mc.getWindow().getScaledHeight() / 2);
        boolean topRight = getX() > (mc.getWindow().getScaledWidth() / 2) && getY() < (mc.getWindow().getScaledHeight() / 2);
        boolean bottomLeft = getX() < (mc.getWindow().getScaledWidth() / 2) && getY() > (mc.getWindow().getScaledHeight() / 2);
        boolean bottomRight = getX() > (mc.getWindow().getScaledWidth() / 2) && getY() > (mc.getWindow().getScaledHeight() / 2);

        if(topLeft || topRight) modules.sort((m1, m2) -> mc.textRenderer.getWidth(m2.getDisplayName()) - mc.textRenderer.getWidth(m1.getDisplayName()));
        else if(bottomLeft || bottomRight) modules.sort(Comparator.comparingInt(m -> mc.textRenderer.getWidth(m.getDisplayName())));
    }

    @Override
    public void draw(DrawContext context) {
        if(nullCheck()) return;

        MatrixStack matrices = context.getMatrices();

        boolean leftHalf = getX() < (mc.getWindow().getScaledWidth() / 2);
        boolean rightHalf = getX() > (mc.getWindow().getScaledWidth() / 2);

        // Initializes the Module list and sorts it
        getModulesAndSort();

        double width = 0;

        // Offset between modules
        double yOffset = 0;
        // Count variable for the color animation
        int count = 0;

        for (Module module : modules) {

            matrices.push();

            // Get the modules animation
            Animation moduleAnimation = module.getAnimation();

            // Set Direction of the Animation
            moduleAnimation.setDirection(module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);

            // If the module is disabled and the animation is finished, skip it
            // 15/04/2023 added check for category toggling modules
            if (!module.isEnabled() && moduleAnimation.finished(Direction.BACKWARDS) || module.getCategory().equals(Category.CATEGORY_MNG)) continue;

            String displayText = module.getDisplayName();
            float textWidth = mc.textRenderer.getWidth(displayText);

            if(textWidth > width) width = textWidth;

            float x = getX();

            float alphaAnimation;
            float y = (float) (yOffset + 1) + getY();

            if (!moduleAnimation.isDone()) {
                RenderUtils.scale(matrices,x, y / 2 - mc.textRenderer.fontHeight / 2f, moduleAnimation.getOutput().floatValue());
            }
            alphaAnimation = moduleAnimation.getOutput().floatValue();

            int index = count * colorSpread.getIntValue();
            Pair<Color, Color> colors = Pair.of(new Color(236, 133, 209), new Color(28, 167, 222));
            Color textcolor = ColorUtil.interpolateColorsBackAndForth(colorSpeed.getIntValue(), index, colors.getFirst(), colors.getSecond(), false);

            Color color = ColorUtil.applyOpacity(textcolor, alphaAnimation);

            if(leftHalf) {
                RenderUtils.drawString(context, displayText, x, y, color.getRGB());
            }
            else if(rightHalf) {
                RenderUtils.drawString(context, displayText, x + getWidth() - textWidth, y, color.getRGB());
            }

            matrices.pop();

            yOffset += moduleAnimation.getOutput().floatValue() * 12;
            count++;
        }

        setHeight((int) yOffset);
        setWidth((int) width);
    }
}
