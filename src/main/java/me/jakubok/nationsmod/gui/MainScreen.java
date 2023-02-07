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
import net.minecraft.text.TranslatableText;

public class MainScreen extends Screen {

    protected final int windowLeft = 0;
    protected final int windowRight = windowLeft + 480;
    
    protected final int windowTop = 0;
    protected final int windowBottom = windowTop + 255;

    protected final int windowCenterHorizontal = (windowLeft + windowRight) / 2;
    protected final int windowCenterVertical = (windowTop + windowBottom) / 2;

    public MainScreen() {
        super(new TranslatableText("gui.nationsmod.main_screen.title"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(
            matrices, 
            this.textRenderer, 
            this.title, 
            windowCenterHorizontal, 
            windowCenterVertical - 50,
            0xffffff
        );
        
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        super.init();

        // windowCenterHorizontal - 100, 
        // windowCenterVertical - 12,

        this.addDrawableChild(new ButtonWidget(
            this.windowCenterHorizontal - 100, 
            windowCenterVertical - 12, 
            200, 
            20, 
            new TranslatableText("gui.nationsmod.main_screen.towns_button"), 
            b -> {
                PlayChannelHandler response = (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
                    NbtCompound compound = buf.readNbt();

                    Map<String, UUID> towns = new HashMap<>();
                    for (int i = 1; i <= compound.getInt("size"); i++)
                        towns.put(compound.getString("town_name" + i), compound.getUuid("town_id" + i));

                    client.execute(() -> {
                        client.setScreen(new TownsScreen(towns));
                    });
                };

                ClientNetworking.makeARequest(Packets.PREPARE_TOWNS_SCREEN, PacketByteBufs.create(), response);
            }
        ));

        this.addDrawableChild(new ButtonWidget(
            windowCenterHorizontal - 100, 
            windowCenterVertical + 12, 
            200, 
            20, 
            new TranslatableText("gui.nationsmod.main_screen.nations_button"), 
            b -> {}
        ));

        this.addDrawableChild(new ButtonWidget(
            windowCenterHorizontal - 100, 
            windowCenterVertical + 36, 
            200, 
            20, 
            new TranslatableText("gui.nationsmod.main_screen.map_button"), 
            b -> {
                this.client.setScreen(new MapScreen(this.client));
            }
        ));
    }
}
