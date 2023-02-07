package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collections.Border;
import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.BorderSlots;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SelectABorderSlot implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        String slotName = buf.readString();

        server.execute(() -> {
            BorderSlots slots = ComponentsRegistry.BORDER_SLOTS.get(player);

            for (BorderGroup slot : slots.slots) {
                if (slot.name.toLowerCase().equals(slotName.toLowerCase())) {
                    slots.selectedSlot = slots.slots.indexOf(slot);

                    for (Border block : slot.toList()) {
                        PacketByteBuf buffer = PacketByteBufs.create();
                        buffer.writeBlockPos(block.position);

                        ServerPlayNetworking.send(player, Packets.HIGHLIGHT_A_BLOCK, buffer);
                    }

                    return;
                }
            }
        });
    }
    
}
