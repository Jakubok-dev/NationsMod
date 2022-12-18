package me.jakubok.nationsmod.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

public class MapScreen extends Screen {

    public double centreX, centreZ;
    public int playerX = 0, playerZ = 0;
    public ButtonWidget plus, minus;
    final MinecraftClient client;
    public double scale = 1.25d;

    public MapScreen(MinecraftClient client) {
        super(Text.of("Map"));
        this.client = client;
        if (client.player != null) {
            centreX = client.player.getBlockX();
            centreZ = client.player.getBlockZ();
            playerX = client.player.getBlockX();
            playerZ = client.player.getBlockZ();
        } else {
            centreX = 0d;
            centreZ = 0d;
        }
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        final Tessellator tesselator = Tessellator.getInstance();
		final BufferBuilder buffer = tesselator.getBuffer();

        int x = (int)(centreX - (this.width / scale / 2));
        int z = (int)(centreZ - (this.height / scale / 2));
        buffer.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.enableBlend();
        for (double i = 0d; i < width; i += scale) {
            for (double j = 0d; j < height; j += scale) {
                final int blockHeight = this.client.world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z) - 1;
                int colour = this.client.world.getBlockState(new BlockPos(
                        x,
                        blockHeight,
                        z
                    )).getBlock().getDefaultMapColor().color;

                final int r = (colour >> 16) & 0xFF;
                final int g = (colour >> 8) & 0xFF;
                final int b = colour & 0xFF;

                buffer.vertex(i, j, 0).color(r, g, b, 255).next();
                buffer.vertex(i, j + scale, 0).color(r, g, b, 255).next();
                buffer.vertex(i + scale, j + scale, 0).color(r, g, b, 255).next();
                buffer.vertex(i + scale, j, 0).color(r, g, b, 255).next();

                z++;
            }
            z = (int)(centreZ - (this.height / scale / 2));
            x++;
        }
        tesselator.draw();
        RenderSystem.disableBlend();
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        super.init();
        this.plus = new ButtonWidget(
            0, 
            0, 
            20, 
            20, 
            Text.of("+"), 
            t -> {
                scale = scale * 1.25d;
            }
        );
        this.addDrawableChild(this.plus);
        this.minus = new ButtonWidget(
            20, 
            0, 
            20, 
            20, 
            Text.of("-"), 
            t -> {
                scale = scale / 1.25d;
            }
        );
        this.addDrawableChild(this.minus);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        centreX -= deltaX / scale;
        centreZ -= deltaY / scale;
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (amount < 0) 
            scale /= Math.pow(1.25d, -amount);
        else
            scale *= Math.pow(1.25d, amount);
        return true;
    }
}
