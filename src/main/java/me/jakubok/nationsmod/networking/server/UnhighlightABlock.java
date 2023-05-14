package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collection.Border;
import me.jakubok.nationsmod.collection.BorderSlots;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class UnhighlightABlock implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();

        server.execute(() -> {
            BorderSlots slots = PlayerInfo.fromAccount(new PlayerAccount(player), server).slots;
            Border border = new Border(pos);
            slots.getSelectedSlot().delete(border);
        });
    }
    
}
