package me.jakubok.nationsmod.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class SimpleWindow extends Screen {

    public static final int windowLeft = 120;
    public static final int windowRight = windowLeft + 248;
    
    public static final int windowTop = 50;
    public static final int windowBottom = windowTop + 165;

    public static final int windowCenterHorizontal = (windowLeft + windowRight) / 2;
    public static final int windowCenterVertical = (windowTop + windowBottom) / 2;

    public SimpleWindow(Text title) {
        super(title);
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/gui/demo_background.png"));

        drawTexture(matrices, 120, 50, 0, 0, 256, 256, 256, 256);

        drawCenteredText(
            matrices, 
            this.textRenderer, 
            this.title, 
            windowCenterHorizontal, 
            windowTop + 10,
            0xffffff
        );

        super.render(matrices, mouseX, mouseY, delta);
    }

    public TextRenderer getTextRenderer() {
        return this.textRenderer;
    }

    public MinecraftClient getClient() {
        return this.client;
    }
}
