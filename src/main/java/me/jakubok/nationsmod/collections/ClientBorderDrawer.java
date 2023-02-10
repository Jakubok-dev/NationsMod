package me.jakubok.nationsmod.collections;

import java.util.HashMap;
import java.util.Map;

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

    Map<BlockPos, Colour> storage = new HashMap<>();

    public void emptyStorage() {
        this.storage.clear();
    }

    public boolean highLightABlock(BlockPos pos, Colour colour) {
        if (this.storage.get(pos) != null) return false;
        this.storage.put(pos, colour);
        return true;
    }

    public boolean unhighlightABlock(BlockPos pos, Colour colour) {
        if (this.storage.get(pos) == null)
            return false;
        if (!this.storage.get(pos).equals(colour))
            return false;
        this.storage.remove(pos);
        return true;
    }

    public void render(float partialTicks, MatrixStack stack) {
        for (BlockPos blocksPosition : this.storage.keySet()) {
            MinecraftClient client = MinecraftClient.getInstance();
            BlockPos playersPos = client.cameraEntity.getBlockPos();

            this.drawABox(stack, new BlockPos(blocksPosition.getX(), playersPos.getY(), blocksPosition.getZ()), this.storage.get(blocksPosition));
        }
    }

    private void drawABox(MatrixStack stack, BlockPos blockPosition, Colour colour) {
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

        buffer.vertex(model, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY(), blockPosition.getZ()).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY(), blockPosition.getZ() + 1).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        buffer.vertex(model, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ() + 1).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        buffer.vertex(model, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();

        buffer.vertex(model, blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ()).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();

        buffer.vertex(model, blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ()).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY() + 1, blockPosition.getZ()).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY() + 1, blockPosition.getZ() + 1).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        buffer.vertex(model, blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ() + 1).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        buffer.vertex(model, blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ()).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();

        buffer.vertex(model, blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ() + 1).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        buffer.vertex(model, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ() + 1).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();

        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY(), blockPosition.getZ() + 1).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY() + 1, blockPosition.getZ() + 1).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();

        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY() + 1, blockPosition.getZ()).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        buffer.vertex(model, blockPosition.getX() + 1, blockPosition.getY(), blockPosition.getZ()).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f)
        .next();
        
        tessellator.draw();

        RenderSystem.enableTexture();
        RenderSystem.lineWidth(1.0f);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        stack.pop();
    }
}
