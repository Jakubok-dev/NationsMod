package me.jakubok.nationsmod.gui;


import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TownCreationScreen extends Screen {

    private static final Identifier WINDOW = new Identifier("minecraft", "textures/gui/container/villager2.png");

    public TownCreationScreen(Text title) {
        super(title);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/gui/demo_background.png"));

        this.drawTexture(matrices, 256/2, 50, 0, 0, 256, 256);

        this.drawCenteredText(matrices, this.textRenderer, this.title, 256, 60, 0x888888);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
	protected void init() {
        super.init();
        
    }
}
