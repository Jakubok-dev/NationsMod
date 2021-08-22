package me.jakubok.nationsmod.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.GameModeSelectionScreen.ButtonWidget;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class TownsScreen extends HandledScreen<ScreenHandler> {

    protected ButtonWidget left;
    protected ButtonWidget right;

    protected final int windowLeft = 120;
    protected final int windowRight = windowLeft + 248;
    
    protected final int windowTop = 50;
    protected final int windowBottom = windowTop + 165;

    protected final int windowCenterHorizontal = (windowLeft + windowRight) / 2;
    protected final int windowCenterVertical = (windowTop + windowBottom) / 2;

    public TownsScreen(ScreenHandler handler, PlayerInventory inventory) {
        super(handler, inventory, new TranslatableText("gui.nationsmod.towns_screen.title"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        renderBackground(matrices);

        RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/gui/demo_background.png"));
        drawTexture(matrices, 120, 50, 0, 0, 256, 256, 256, 256);

        // Towns
        drawCenteredText(
            matrices, 
            this.textRenderer, 
            this.title, 
            windowCenterHorizontal, 
            windowTop + 10,
            0xffffff
        );
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {}
}
