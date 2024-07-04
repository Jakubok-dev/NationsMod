package me.jakubok.nationsmod.collection;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jakubok.nationsmod.geometry.Point;
import me.jakubok.nationsmod.geometry.Polygon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public class ClientPolygonDrawer {
    Map<String, Polygon> playersPolygons = new HashMap<>();

    public void emptyStorage() {
        this.playersPolygons.clear();
    }

    public Polygon addAPolygon(Polygon polygon) {
        return this.playersPolygons.put(polygon.name, polygon);
    }

    public Polygon removeAPolygon(String name) {
        return this.playersPolygons.remove(name);
    }

    public void render(float partialTicks, MatrixStack stack) {
        for (Polygon polygon : this.playersPolygons.values()) {
            if (polygon.isEmpty())
                continue;
            MinecraftClient client = MinecraftClient.getInstance();
            BlockPos playersPos = client.cameraEntity.getBlockPos();
            PolygonNode<Point> node = polygon.root;
            while (node.right != null) {
                this.drawABox(stack, new BlockPos(node.value.key, playersPos.getY(),node.value.value), new Colour(100, 100, 200));
                this.drawALine(stack, node, playersPos, new Colour(255, 255, 255));
                node = node.right;
                if (node == polygon.root)
                    break;
            }
            if (node != polygon.root || polygon.size() == 1)
                this.drawABox(stack, new BlockPos(node.value.key, playersPos.getY(),node.value.value), new Colour(100, 100, 200));
        }
    }

    private void drawALine(MatrixStack stack, PolygonNode<Point> node, BlockPos playerPos, Colour colour) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        Vec3d cameraPosition = camera.getPos();

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();

        stack.push();
        stack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
        Matrix4f model = stack.peek().getPositionMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

        RenderSystem.lineWidth(4.0f);

        buffer.vertex(model, node.value.key + 0.5f, playerPos.getY() + 0.5f, node.value.value + 0.5f).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f).next();
        buffer.vertex(model, node.right.value.key + 0.5f, playerPos.getY() + 0.5f, node.right.value.value + 0.5f).color((float)colour.getR()/255f, (float)colour.getG()/255f, (float)colour.getB()/255f, 1.0f).next();
        tessellator.draw();

        RenderSystem.lineWidth(1.0f);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        stack.pop();
    }

    private void drawABox(MatrixStack stack, BlockPos blockPosition, Colour colour) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        Vec3d cameraPosition = camera.getPos();

        // This code was stolen from https://github.com/ModProg/BlockMeter/blob/0171696b2253968c2dc2dcc9b0aa6f91e033aa79/src/main/java/win/baruna/blockmeter/measurebox/ClientMeasureBox.java

        RenderSystem.lineWidth(2.0f);

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();

        stack.push();
        stack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
        Matrix4f model = stack.peek().getPositionMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

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

        RenderSystem.lineWidth(1.0f);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        stack.pop();
    }
}
