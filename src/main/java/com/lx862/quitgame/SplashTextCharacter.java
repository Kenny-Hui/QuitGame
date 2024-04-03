package com.lx862.quitgame;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector2d;

public class SplashTextCharacter {
    private final char character;
    private Vector2d startPos;
    private Vector2d targetPos;
    private Vector2d renderedPos;
    public double width;
    private boolean dragging;

    public SplashTextCharacter(char character) {
        this.character = character;
        this.targetPos = new Vector2d(0, 0);
        this.renderedPos = new Vector2d(0, 0);
        this.width = MinecraftClient.getInstance().textRenderer.getWidth(String.valueOf(character));
    }

    public char getChar() {
        return character;
    }

    public void setStartPos(double x, double y) {
        this.startPos = new Vector2d(x, y);
    }

    public void setTargetPos(double x, double y) {
        this.targetPos = new Vector2d(x, y);
    }

    public void setRenderPos(double x, double y) {
        this.renderedPos = new Vector2d(x, y);
    }

    public boolean hovered(double mouseX, double mouseY) {
        return hoveredXAxis(mouseX) && hoveredYAxis(mouseY);
    }

    public boolean hoveredXAxis(double mouseX) {
        double startX = (startPos.x) + (targetPos.x * QuitGame.scale);
        double endX = startX + ((width + 0.5) * QuitGame.scale);
        return mouseX >= startX && mouseX <= endX;
    }

    public boolean hoveredYAxis(double mouseY) {
        double startY = (startPos.y) + ((targetPos.y - 1) * QuitGame.scale);
        double endY = startY + ((8 + 2) * QuitGame.scale);
        return mouseY >= startY && mouseY <= endY;
    }

    public void render(DrawContext drawContext, double deltaTime, int alpha, TextRenderer textRenderer) {
        renderedPos = renderedPos.lerp(targetPos, deltaTime);

        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(renderedPos.x, renderedPos.y, 0);
        drawContext.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-QuitGame.rotAngle));
        drawContext.drawTextWithShadow(textRenderer, String.valueOf(character), 0, 0, 16776960 | alpha);
        drawContext.getMatrices().pop();
    }

    public void renderBoundary(DrawContext drawContext, double deltaTime, TextRenderer textRenderer) {
        double startX = (startPos.x) + (targetPos.x * QuitGame.scale);
        double startY = (startPos.y) + ((targetPos.y - 1) * QuitGame.scale);
        double endX = ((width + 0.5) * QuitGame.scale);
        double endY = ((8 + 2) * QuitGame.scale);

        drawContext.drawBorder((int)startX, (int)startY, (int)endX, (int)endY, 0xFFFFFFFF);
    }

    public void dragged() {
        dragging = true;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void released() {
        dragging = false;
    }
}
