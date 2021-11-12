package me.jakubok.nationsmod.networking.client;

import me.jakubok.nationsmod.NationsClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UnhighlightABlockPacketReceiver implements PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
            PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();

        client.execute(() -> {
            NationsClient.drawer.unhighlightABlock(pos);
        });
    }
    
}
