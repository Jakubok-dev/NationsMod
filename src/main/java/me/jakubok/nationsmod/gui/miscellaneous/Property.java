package me.jakubok.nationsmod.gui.miscellaneous;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class Property {
    public Text property, value;
    public MinecraftClient client;
    private int y;
    
    public void render(MatrixStack matrices, ResizableWindow parentScreen, TextRenderer textRenderer, int mouseX, int mouseY, float delta) {
        int valueWidth = textRenderer.getWidth(value);
        Screen.drawTextWithShadow(
            matrices,
            textRenderer,
            property,
            parentScreen.getWindowLeft() + 20,
            y,
            0xFFFFFF
        );
        
        Screen.drawTextWithShadow(
            matrices,
            textRenderer,
            value,
            parentScreen.getWindowRight() - 20 - valueWidth,
            y,
            0xFFFFFF
        );
    }

    public Property(Text property, Text value, MinecraftClient client, int y) {
        this.property = property;
        this.value = value;
        this.client = client;
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
