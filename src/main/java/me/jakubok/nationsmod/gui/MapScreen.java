package me.jakubok.nationsmod.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;

import me.jakubok.nationsmod.NationsClient;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.collection.Border;
import me.jakubok.nationsmod.collection.Colour;
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
    public ButtonWidget plus, minus, borderSlots, drawing, correction;
    protected boolean drawingMode = false, autocorrection = true;
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
        drawTextWithShadow(matrices, textRenderer, Text.of("X: " + this.getBlockX(mouseX) + " Z: " + this.getBlockZ(mouseY)), 0, this.height - 10, 0xFFFFFF);
        this.mouseHovered(matrices, mouseX, mouseY);
        this.drawing.active = NationsClient.selectedSlot != -1;
        super.render(matrices, mouseX, mouseY, delta);
    }

    protected void mouseHovered(MatrixStack matrices, int mouseX, int mouseY) {
        if (mouseX >= 120 && mouseX < 240 && mouseY >= 0 && mouseY <= 20) {
            List<Text> lines = new ArrayList<>();
            lines.add(Text.translatable("gui.nationsmod.map_screen.drawing_tooltip_1"));
            lines.add(Text.translatable("gui.nationsmod.map_screen.drawing_tooltip_2"));

            if (NationsClient.selectedSlot == -1) {
                lines.add(Text.translatable("gui.nationsmod.map_screen.drawing_tooltip_3"));
                lines.add(Text.translatable("gui.nationsmod.map_screen.drawing_tooltip_4"));
            }
            this.renderTooltip(matrices, lines, mouseX, mouseY);
            return;
        } else if (mouseX >= 240 && mouseX < 380 && mouseY >= 0 && mouseY <= 20) {
            List<Text> lines = new ArrayList<>(); 
            lines.add(Text.translatable("gui.nationsmod.map_screen.autocorrection_tooltip_1"));
            lines.add(Text.translatable("gui.nationsmod.map_screen.autocorrection_tooltip_2"));
            lines.add(Text.translatable("gui.nationsmod.map_screen.autocorrection_tooltip_3"));
            this.renderTooltip(matrices, lines, mouseX, mouseY);
            return;
        }
        int blockx = this.getBlockX(mouseX);
        int blockz = this.getBlockZ(mouseY);
        List<Text> temp = NationsClient.map.claimersAtInLines(new BlockPos(blockx, 64, blockz));
        if (temp == null)
            return;
        this.renderTooltip(matrices, temp, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();

        this.plus = ButtonWidget.builder(
            Text.of("+"), 
            t -> {
                scale = scale * 1.25d;
            }
        ).dimensions(
            0, 
            0, 
            20, 
            20
        ).build();
        this.addDrawableChild(this.plus);
        this.minus = ButtonWidget.builder(
            Text.of("-"), 
            t -> {
                scale = scale / 1.25d;
            }
        ).dimensions(
            20, 
            0, 
            20, 
            20
        ).build();
        this.addDrawableChild(this.minus);
        this.borderSlots = ButtonWidget.builder(
            Text.translatable("gui.nationsmod.border_registrator_screen.title"), 
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
        ).dimensions(
            40, 
            0, 
            80, 
            20
        ).build();
        this.addDrawableChild(this.borderSlots);
        this.drawing = ButtonWidget.builder(
            null, 
            t -> {
                if (NationsClient.selectedSlot == -1)
                    return;
                drawingMode = !drawingMode;
                if (drawingMode) {
                    this.drawing.setMessage(Text.translatable("gui.nationsmod.map_screen.stop_drawing"));
                } else {
                    this.drawing.setMessage(Text.translatable("gui.nationsmod.map_screen.start_drawing"));
                }
            }
        ).dimensions(
            120, 
            0, 
            120, 
            20
        ).build();
        if (drawingMode) {
            this.drawing.setMessage(Text.translatable("gui.nationsmod.map_screen.stop_drawing"));
        } else {
            this.drawing.setMessage(Text.translatable("gui.nationsmod.map_screen.start_drawing"));
        }
        this.addDrawableChild(this.drawing);
        this.correction = ButtonWidget.builder(
            null, 
            t -> {
                autocorrection = !autocorrection;
                if (autocorrection) {
                    this.correction.setMessage(Text.translatable("gui.nationsmod.map_screen.turn_off_autocorrection"));
                } else {
                    this.correction.setMessage(Text.translatable("gui.nationsmod.map_screen.turn_on_autocorrection"));
                }
            }
        ).dimensions(
            240, 
            0, 
            140, 
            20
        ).build();
        if (autocorrection) {
            this.correction.setMessage(Text.translatable("gui.nationsmod.map_screen.turn_off_autocorrection"));
        } else {
            this.correction.setMessage(Text.translatable("gui.nationsmod.map_screen.turn_on_autocorrection"));
        }
        this.addDrawableChild(this.correction);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!drawingMode) {
            centreX -= deltaX / scale;
            centreZ -= deltaY / scale;
        } else {
            int x = this.getBlockX(mouseX);
            int z = this.getBlockZ(mouseY);
            
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
                this.highlightABlock(x, z);
                if (autocorrection)
                    this.doAutocorrection(x, z);
            }
        }
        return true;
    }

    private void doAutocorrection(int x, int z) {
        BlockPos posB = new BlockPos(x + 1, 64, z - 1);
        BlockPos posB1 = new BlockPos(x + 1, 64, z);
        BlockPos posB2 = new BlockPos(x, 64, z - 1);
        if (NationsClient.borderSlot.contains(posB) && !NationsClient.borderSlot.contains(posB1) && !NationsClient.borderSlot.contains(posB2))
            this.highlightABlock(x + 1, z);
        BlockPos posC = new BlockPos(x + 1, 64, z + 1);
        BlockPos posC1 = new BlockPos(x + 1, 64, z);
        BlockPos posC2 = new BlockPos(x, 64, z + 1);
        if (NationsClient.borderSlot.contains(posC) && !NationsClient.borderSlot.contains(posC1) && !NationsClient.borderSlot.contains(posC2))
            this.highlightABlock(x + 1, z);
        BlockPos posD = new BlockPos(x - 1, 64, z - 1);
        BlockPos posD1 = new BlockPos(x - 1, 64, z);
        BlockPos posD2 = new BlockPos(x, 64, z - 1);
        if (NationsClient.borderSlot.contains(posD) && !NationsClient.borderSlot.contains(posD1) && !NationsClient.borderSlot.contains(posD2))
            this.highlightABlock(x - 1, z);
        BlockPos posE = new BlockPos(x - 1, 64, z + 1);
        BlockPos posE1 = new BlockPos(x - 1, 64, z);
        BlockPos posE2 = new BlockPos(x, 64, z + 1);
        if (NationsClient.borderSlot.contains(posE) && !NationsClient.borderSlot.contains(posE1) && !NationsClient.borderSlot.contains(posE2))
            this.highlightABlock(x - 1, z);
    }

    private void highlightABlock(int x, int z) {
        NationsClient.borderSlot.insert(new Border(x, z));
        NationsClient.map.renderTheBorderRegistratorLayer(new BlockPos(x, 64, z));
        NationsClient.drawer.highlightABlock(new BlockPos(x, 64, z), new Colour(255, 255, 255));
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(new BlockPos(x, 64, z));
        ClientPlayNetworking.send(Packets.HIGHLIGHT_A_BLOCK_SERVER, buf);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= 0 && mouseX <= 380 && mouseY >= 0 && mouseY <= 20 || this.drawingMode)
            return super.mouseClicked(mouseX, mouseY, button);
        int blockx = this.getBlockX(mouseX);
        int blockz = this.getBlockZ(mouseY);

        UUID townsID = NationsClient.map.townsUUIDAt(new BlockPos(blockx, 64, blockz));
        if (townsID == null)
            return super.mouseClicked(mouseX, mouseY, button);

        PacketByteBuf buffer = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("id", townsID);
        buffer.writeNbt(nbt);

        PlayChannelHandler response = (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
            Town town = new Town(buf.readNbt(), null);

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
    public void close() {
        this.client.setScreen(previousScreen);
    }

    public int getBlockX(double mouseX) {
        int blockx = (int)(Math.floor(mouseX / scale) + (int)(centreX - (this.width / scale / 2)));
        return blockx;
    }
    public int getBlockZ(double mouseY) {
        int blockz = (int)(Math.floor(mouseY / scale) + (int)(centreZ - (this.height / scale / 2)));
        return blockz;
    }
}
