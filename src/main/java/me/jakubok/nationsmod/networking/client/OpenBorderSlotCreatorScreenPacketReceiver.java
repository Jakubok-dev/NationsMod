package me.jakubok.nationsmod.networking.client;

import me.jakubok.nationsmod.gui.BorderSlotCreationScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class OpenBorderSlotCreatorScreenPacketReceiver implements PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
            PacketSender responseSender) {
        client.execute(() -> {
            client.setScreen(new BorderSlotCreationScreen());
        });
    }
    
}
