package me.jakubok.nationsmod.gui.miscellaneous;

import com.mojang.blaze3d.systems.RenderSystem;

import me.jakubok.nationsmod.NationsMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class ResizableWindow extends Screen {

    protected int windowWidth;
    protected int windowHeight;
    protected int borderRadius;

    public int getWindowLeft() {
        return (this.width - windowWidth) / 2;
    }
    public int getWindowTop() {
        return (this.height - windowHeight) / 2;
    }
    public int getWindowRight() {
        return this.getWindowLeft() + windowWidth;
    }
    public int getWindowBottom() {
        return this.getWindowTop() + windowHeight;
    }
    public int getWindowWidth() {
        return windowWidth;
    }
    public int getWindowHeight() {
        return windowHeight;
    }
    public int getBorderRadius() {
        return borderRadius;
    }

    protected int windowCenterHorizontal() { return (this.getWindowLeft() + this.getWindowRight()) / 2; }
    protected int windowCenterVertical() { return (this.getWindowTop() + this.getWindowBottom()) / 2; }

    private final Screen previousScreen;

    public ResizableWindow(Text title, Screen previousScreen) {
        this(title, 248, 165, 4, previousScreen);
    }
    public ResizableWindow(Text title, int width, int height, int borderRadius, Screen previousScreen) {
        super(title);
        this.windowWidth = width;
        this.windowHeight = height;
        this.borderRadius = borderRadius;
        this.previousScreen = previousScreen;
    }

    public void drawBackground(MatrixStack matrices, int x, int y, int width, int height, int outerSliceSize, int u, int v, int regionOuterSliceSize, int textureWidth, int textureHeight) {
        RenderSystem.setShaderTexture(0, new Identifier(NationsMod.MOD_ID, "textures/gui/panel_light.png"));
        // Corners
        drawTexture(matrices, x, y, outerSliceSize, outerSliceSize, u, v, regionOuterSliceSize, regionOuterSliceSize, textureWidth, textureHeight);
        drawTexture(matrices, x + width - outerSliceSize, y, outerSliceSize, outerSliceSize, u + textureWidth - regionOuterSliceSize, v, regionOuterSliceSize, regionOuterSliceSize, textureWidth, textureHeight);
        drawTexture(matrices, x, y + height - outerSliceSize, outerSliceSize, outerSliceSize, u, v + textureHeight - regionOuterSliceSize, regionOuterSliceSize, regionOuterSliceSize, textureWidth, textureHeight);
        drawTexture(matrices, x + width - outerSliceSize, y + height - outerSliceSize, outerSliceSize, outerSliceSize, u + textureWidth - regionOuterSliceSize, v + textureHeight - regionOuterSliceSize, regionOuterSliceSize, regionOuterSliceSize, textureWidth, textureHeight);
        // Top and bottom bars
        drawTexture(matrices, x + outerSliceSize, y, width - 2 * outerSliceSize, outerSliceSize, u + regionOuterSliceSize, v, textureWidth - 2 * regionOuterSliceSize, regionOuterSliceSize, textureWidth, textureHeight);
        drawTexture(matrices, x + outerSliceSize, y + height - outerSliceSize, width - 2 * outerSliceSize, outerSliceSize, u + regionOuterSliceSize, v + textureHeight - regionOuterSliceSize, textureWidth - 2 * regionOuterSliceSize, regionOuterSliceSize, textureWidth, textureHeight);
        // Left and right bars
        drawTexture(matrices, x, y + outerSliceSize, outerSliceSize, height - 2 * outerSliceSize, u, v + regionOuterSliceSize, regionOuterSliceSize, textureHeight - 2 * regionOuterSliceSize, textureWidth, textureHeight);
        drawTexture(matrices, x + width - outerSliceSize, y + outerSliceSize, outerSliceSize, height - 2 * outerSliceSize, u + textureWidth - regionOuterSliceSize, v + regionOuterSliceSize, regionOuterSliceSize, textureHeight - 2 * regionOuterSliceSize, textureWidth, textureHeight);
        // Centre
        drawTexture(matrices, x + outerSliceSize, y + outerSliceSize, width - 2 * outerSliceSize, height - 2 * outerSliceSize, u + regionOuterSliceSize, v + regionOuterSliceSize, textureWidth - 2 * regionOuterSliceSize, textureHeight - 2 * regionOuterSliceSize, textureWidth, textureHeight);
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        drawBackground(matrices, this.getWindowLeft(), this.getWindowTop(), this.windowWidth, this.windowHeight, this.getBorderRadius(), 0, 0, 4, 16, 16);
        drawCenteredTextWithShadow(
            matrices, 
            this.textRenderer, 
            this.title, 
            this.windowCenterHorizontal(),
            this.getWindowTop() + 10,
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

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(previousScreen);
    }

    @Override
    protected void init() {
        this.clearChildren();
        super.init();
    }
}
