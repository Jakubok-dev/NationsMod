package me.jakubok.nationsmod.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class SimpleWindow extends Screen {

    protected final int windowLeft = 120;
    protected final int windowRight = windowLeft + 248;
    
    protected final int windowTop = 50;
    protected final int windowBottom = windowTop + 165;

    protected final int windowCenterHorizontal = (windowLeft + windowRight) / 2;
    protected final int windowCenterVertical = (windowTop + windowBottom) / 2;

    protected SimpleWindow(Text title) {
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
}
