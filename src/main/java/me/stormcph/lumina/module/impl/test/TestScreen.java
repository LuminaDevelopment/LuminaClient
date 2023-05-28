package me.stormcph.lumina.module.impl.test;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class TestScreen extends Screen {

    private boolean draggingSquare = false;
    private boolean draggingResizeSquare = false;
    private double squareX;
    private double squareY;
    private double resizeSquareX;
    private double resizeSquareY;
    private int squareSize = 100;
    private int resizeSquareSize = 20;

    protected TestScreen() {
        super(Text.of("Test Screen"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        // Draw the main square
        fill(matrices, (int) squareX, (int) squareY, (int) (squareX + squareSize), (int) (squareY + squareSize), 0xFF0000FF);

        // Calculate the position of the resize square based on the main square
        resizeSquareX = squareX + squareSize;
        resizeSquareY = squareY + squareSize;

        // Draw the resize square
        fill(matrices, (int) resizeSquareX, (int) resizeSquareY, (int) (resizeSquareX + resizeSquareSize), (int) (resizeSquareY + resizeSquareSize), 0xFFFF0000);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (isMouseOverSquare(mouseX, mouseY)) {
                draggingSquare = true;
                return true;
            } else if (isMouseOverResizeSquare(mouseX, mouseY)) {
                draggingResizeSquare = true;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            draggingSquare = false;
            draggingResizeSquare = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingSquare) {
            // Update the position of the main square based on the mouse movement
            squareX = MathHelper.clamp(squareX + deltaX, 0, width - squareSize);
            squareY = MathHelper.clamp(squareY + deltaY, 0, height - squareSize);
            return true;
        } else if (draggingResizeSquare) {
            // Calculate the average change in mouse position to ensure proportional resizing
            double averageDelta = (deltaX + deltaY) / 2;

            // Update the size of the main square based on the mouse movement
            squareSize = MathHelper.clamp((int) (squareSize + averageDelta), 10, Math.min(width - (int) squareX, height - (int) squareY));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private boolean isMouseOverSquare(double mouseX, double mouseY) {
        return mouseX >= squareX && mouseX <= squareX + squareSize &&
                mouseY >= squareY && mouseY <= squareY + squareSize;
    }

    private boolean isMouseOverResizeSquare(double mouseX, double mouseY) {
        return mouseX >= resizeSquareX && mouseX <= resizeSquareX + resizeSquareSize &&
                mouseY >= resizeSquareY && mouseY <= resizeSquareY + resizeSquareSize;
    }
}
