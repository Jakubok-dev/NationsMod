package me.jakubok.nationsmod.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;

import me.jakubok.nationsmod.NationsClient;
import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.collections.Border;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.gui.townScreen.TownScreen;
import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class MapScreen extends Screen {

    public double centreX, centreZ;
    public int playerX = 0, playerZ = 0;
    public ButtonWidget plus, minus, borderSlots, drawing;
    protected boolean drawingMode = false;
    protected MODE mode = MODE.NONE;
    final MinecraftClient client;
    public double scale = 1.25d;
    private final Screen previousScreen;

    enum MODE {
        HIGHLIGHTING,
        UNHIGHLIGHTING,
        NONE
    };

    public MapScreen(MinecraftClient client, Screen previousScreen) {
        super(Text.of("Map"));
        this.client = client;
        this.previousScreen = previousScreen;
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
                Colour colour = NationsClient.map.theColourAt(new BlockPos(x, 64, z));
                if (colour != null) {
                    buffer.vertex(i, j, 0).color(colour.getR(), colour.getG(), colour.getB(), 255).next();
                    buffer.vertex(i, j + scale, 0).color(colour.getR(), colour.getG(), colour.getB(), 255).next();
                    buffer.vertex(i + scale, j + scale, 0).color(colour.getR(), colour.getG(), colour.getB(), 255).next();
                    buffer.vertex(i + scale, j, 0).color(colour.getR(), colour.getG(), colour.getB(), 255).next();
                }
                z++;
            }
            z = (int)(centreZ - (this.height / scale / 2));
            x++;
        }
        tesselator.draw();
        RenderSystem.disableBlend();
        drawTextWithShadow(matrices, textRenderer, Text.of("X: " + (int)(Math.floor(mouseX / scale) + Math.floor(centreX - this.width / scale / 2)) + " Z: " + (int)(Math.floor(mouseY / scale) + Math.floor(centreZ - this.height / scale / 2))), 0, this.height - 10, 0xFFFFFF);
        this.mouseHovered(matrices, mouseX, mouseY);
        super.render(matrices, mouseX, mouseY, delta);
    }

    protected void mouseHovered(MatrixStack matrices, int mouseX, int mouseY) {
        int blockx = (int)(Math.floor(mouseX / scale) + Math.floor(centreX - this.width / scale / 2));
        int blockz = (int)(Math.floor(mouseY / scale) + Math.floor(centreZ - this.height / scale / 2));
        
        String temp = NationsClient.map.claimersAtAsString(new BlockPos(blockx, 64, blockz));
        if (temp == null)
            return;
        this.renderTooltip(matrices, Text.of(temp), mouseX, mouseY);
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
        this.borderSlots = new ButtonWidget(
            40, 
            0, 
            80, 
            20, 
            Text.of("Border slots"), 
            t -> {
                PlayChannelHandler responseFunction = (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
                    NbtCompound nbt = buf.readNbt();
                    Map<String, Integer> slots = new HashMap<>();
                    for (int i = 0; i < nbt.getInt("size"); i++)
                        slots.put(nbt.getString("name" + i), nbt.getInt("index" + i));
                    client.execute(() -> {
                        client.setScreen(new BorderRegistratorScreen(slots, this));
                    });
                };
                ClientNetworking.makeARequest(Packets.PREPARE_BORDER_REGISTRATOR_SCREEN, PacketByteBufs.create(), responseFunction);
            }
        );
        this.addDrawableChild(this.borderSlots);
        this.drawing = new ButtonWidget(
            120, 
            0, 
            120, 
            20, 
            Text.of("Start drawing"), 
            t -> {
                if (NationsClient.selectedSlot == -1)
                    return;
                drawingMode = !drawingMode;
                if (drawingMode) {
                    this.drawing.setMessage(Text.of("Stop drawing"));
                } else {
                    this.drawing.setMessage(Text.of("Start drawing"));
                }
            }
        );
        this.addDrawableChild(this.drawing);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!drawingMode) {
            centreX -= deltaX / scale;
            centreZ -= deltaY / scale;
        } else {
            int x = (int)(Math.floor(mouseX / scale) + Math.floor(centreX - this.width / scale / 2));
            int z = (int)(Math.floor(mouseY / scale) + Math.floor(centreZ - this.height / scale / 2));
            
            if (this.mode == MODE.NONE)
                this.mode = NationsClient.borderSlot.contains(x, z) ? MODE.UNHIGHLIGHTING : MODE.HIGHLIGHTING;
            
            if (this.mode == MODE.UNHIGHLIGHTING) {
                NationsClient.map.clearTheBorderRegistratorLayer(new BlockPos(x, 64, z));
                NationsClient.borderSlot.delete(x, z);
                NationsClient.drawer.unhighlightABlock(new BlockPos(x, 64, z), new Colour(255, 255, 255));
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(new BlockPos(x, 64, z));
                ClientPlayNetworking.send(Packets.UNHIGHLIGHT_A_BLOCK_SERVER, buf);
            } else {
                NationsClient.borderSlot.insert(new Border(x, z));
                NationsClient.map.renderTheBorderRegistratorLayer(new BlockPos(x, 64, z));
                NationsClient.drawer.highlightABlock(new BlockPos(x, 64, z), new Colour(255, 255, 255));
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(new BlockPos(x, 64, z));
                ClientPlayNetworking.send(Packets.HIGHLIGHT_A_BLOCK_SERVER, buf);
            }
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int blockx = (int)(Math.floor(mouseX / scale) + Math.floor(centreX - this.width / scale / 2));
        int blockz = (int)(Math.floor(mouseY / scale) + Math.floor(centreZ - this.height / scale / 2));

        UUID townsID = NationsClient.map.townsUUIDAt(new BlockPos(blockx, 64, blockz));
        if (townsID == null)
            return super.mouseClicked(mouseX, mouseY, button);

        PacketByteBuf buffer = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("id", townsID);
        buffer.writeNbt(nbt);

        PlayChannelHandler response = (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
            Town town = new Town(buf.readNbt(), client.world.getLevelProperties());

            client.execute(() -> {
                client.setScreen(new TownScreen(town, this));
            });
        };
        ClientNetworking.makeARequest(Packets.PREPARE_TOWN_SCREEN, buffer, response);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.mode = MODE.NONE;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (amount < 0) 
            scale /= Math.pow(1.25d, -amount);
        else
            scale *= Math.pow(1.25d, amount);
        return true;
    }

    @Override
    public void onClose() {
        this.client.setScreen(previousScreen);
    }
}
