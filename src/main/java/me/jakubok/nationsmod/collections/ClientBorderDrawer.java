package me.jakubok.nationsmod.collections;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

public class ClientBorderDrawer {

    List<BlockPos> storage = new ArrayList<>();
    public float red = 1.0f, green = 1.0f, blue = 1.0f, alpha = 0.8f;

    public void emptyStorage() {
        storage.clear();
    }

    public void highLightABlock(BlockPos pos) {
        List<BlockPos> filteredStorage = this.storage.stream()
        .filter(blockpos -> blockpos.getX() == pos.getX() && blockpos.getY() == pos.getY() && blockpos.getZ() == pos.getZ())
        .toList();

        if (filteredStorage.size() == 0)
            this.storage.add(pos);
    }

    public void unhighlightABlock(BlockPos pos) {
        for (int i = 0; i < this.storage.size(); i++) {
            if (
                this.storage.get(i).getX() == pos.getX() &&
                this.storage.get(i).getY() == pos.getY() &&
                this.storage.get(i).getZ() == pos.getZ()
            ) {
                this.storage.remove(i);
                return;
            }
        }
    }

    public void render(float partialTicks, MatrixStack stack) {
        for (int i = 0; i < storage.size(); i++) {
            BlockPos blocksPosition = storage.get(i);
            MinecraftClient client = MinecraftClient.getInstance();
            BlockPos playersPos = client.cameraEntity.getBlockPos();

            this.drawABox(stack, new BlockPos(blocksPosition.getX(), playersPos.getY() - 1, blocksPosition.getZ()));

        }
    }

    private void drawABox(MatrixStack stack, BlockPos blockPosition) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        Vec3d cameraPosition = camera.getPos();

        // This code was stolen from https://github.com/ModProg/BlockMeter/blob/0171696b2253968c2dc2dcc9b0aa6f91e033aa79/src/main/java/win/baruna/blockmeter/measurebox/ClientMeasureBox.java

        RenderSystem.lineWidth(2.0f);
        
        RenderSystem.disableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();

        stack.push();
        stack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
        Matrix4f model = stack.peek().getPositionMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

        buffer.vertex(model, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()).color(red, green, blue, alpha)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY(), blockPosition.getZ()).color(red, green, blue, alpha)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY(), blockPosition.getZ() + 1).color(red, green, blue, alpha)
        .next();
        buffer.vertex(model, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ() + 1).color(red, green, blue, alpha)
        .next();
        buffer.vertex(model, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()).color(red, green, blue, alpha)
        .next();

        buffer.vertex(model, blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ()).color(red, green, blue, alpha)
        .next();

        buffer.vertex(model, blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ()).color(red, green, blue, alpha)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY() + 1, blockPosition.getZ()).color(red, green, blue, alpha)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY() + 1, blockPosition.getZ() + 1).color(red, green, blue, alpha)
        .next();
        buffer.vertex(model, blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ() + 1).color(red, green, blue, alpha)
        .next();
        buffer.vertex(model, blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ()).color(red, green, blue, alpha)
        .next();

        buffer.vertex(model, blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ() + 1).color(red, green, blue, alpha)
        .next();
        buffer.vertex(model, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ() + 1).color(red, green, blue, alpha)
        .next();

        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY(), blockPosition.getZ() + 1).color(red, green, blue, alpha)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY() + 1, blockPosition.getZ() + 1).color(red, green, blue, alpha)
        .next();

        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY() + 1, blockPosition.getZ()).color(red, green, blue, alpha)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY(), blockPosition.getZ()).color(red, green, blue, alpha)
        .next();
        
        tessellator.draw();

        RenderSystem.enableTexture();
        RenderSystem.lineWidth(1.0f);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        stack.pop();
    }
}
