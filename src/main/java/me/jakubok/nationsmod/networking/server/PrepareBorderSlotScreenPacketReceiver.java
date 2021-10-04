package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class PrepareBorderSlotScreenPacketReceiver implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        int index = buf.readInt();

        server.execute(() -> {
            if (index == -1) {
                ServerPlayNetworking.send(player, Packets.OPEN_BORDER_SLOT_CREATOR_SCREEN_PACKET, PacketByteBufs.create());
                return;
            }
        });
    }
    
}
