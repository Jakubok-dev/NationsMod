package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collections.BorderSlots;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class UnselectABorderSlot implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
            server.execute(() -> {
                BorderSlots slots = ComponentsRegistry.BORDER_SLOTS.get(player);
                slots.selectedSlot = -1;
            });
    }
    
}
