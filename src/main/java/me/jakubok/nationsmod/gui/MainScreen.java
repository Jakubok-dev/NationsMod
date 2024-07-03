package me.jakubok.nationsmod.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class MainScreen extends Screen {

    protected int windowWidth;
    protected int windowHeight;

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

    protected int windowCenterHorizontal() { return (this.getWindowLeft() + this.getWindowRight()) / 2; }
    protected int windowCenterVertical() { return (this.getWindowTop() + this.getWindowBottom()) / 2; }

    public MainScreen() {
        super(Text.translatable("gui.nationsmod.main_screen.title"));
        this.windowWidth = 480;
        this.windowHeight = 255;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredTextWithShadow(
            matrices, 
            this.textRenderer, 
            this.title, 
            this.windowCenterHorizontal(),
            this.windowCenterVertical() - 50,
            0xffffff
        );
        
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        super.init();

        // windowCenterHorizontal - 100, 
        // windowCenterVertical - 12,

        this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("gui.nationsmod.main_screen.map_button"), 
            b -> {
                this.client.setScreen(new MapScreen(this.client, this));
            }
        ).dimensions(
            this.windowCenterHorizontal() - 100,
            this.windowCenterVertical() - 12,
            200, 
            20
        ).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("gui.nationsmod.main_screen.towns_button"), 
            b -> {
                PlayChannelHandler response = (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
                    NbtCompound compound = buf.readNbt();

                    Map<String, UUID> towns = new HashMap<>();
                    for (int i = 1; i <= compound.getInt("size"); i++)
                        towns.put(compound.getString("town_name" + i), compound.getUuid("town_id" + i));

                    client.execute(() -> {
                        client.setScreen(new TownsScreen(towns, this));
                    });
                };

                ClientNetworking.makeARequest(Packets.PREPARE_TOWNS_SCREEN, PacketByteBufs.create(), response);
            }
        ).dimensions(
            this.windowCenterHorizontal() - 100,
            this.windowCenterVertical() + 12,
            200, 
            20
        ).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("gui.nationsmod.main_screen.nations_button"), 
            b -> {}
        ).dimensions(
            this.windowCenterHorizontal() - 100,
            this.windowCenterVertical() + 36,
            200, 
            20
        ).build());
    }
}
