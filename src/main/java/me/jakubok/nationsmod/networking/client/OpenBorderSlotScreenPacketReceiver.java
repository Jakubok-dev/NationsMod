package me.jakubok.nationsmod.networking.client;

import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.gui.BorderSlotScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class OpenBorderSlotScreenPacketReceiver implements PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
            PacketSender responseSender) {
        BorderGroup slot = new BorderGroup(buf.readNbt());

        client.execute(() -> {
            client.setScreen(new BorderSlotScreen(slot));
        });
    }
    
}
