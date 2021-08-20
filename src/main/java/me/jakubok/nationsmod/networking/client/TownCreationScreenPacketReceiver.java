package me.jakubok.nationsmod.networking.client;

import me.jakubok.nationsmod.gui.TownCreationScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class TownCreationScreenPacketReceiver {

    @Environment(EnvType.CLIENT)
    public static void handle(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        
        minecraftClient.execute(() -> {
            minecraftClient.setScreen(new TownCreationScreen(Text.of("Town Creation Screen")));
        });
    }
    
}
