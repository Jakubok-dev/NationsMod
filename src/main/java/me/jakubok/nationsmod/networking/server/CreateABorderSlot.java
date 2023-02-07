package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.BorderSlots;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class CreateABorderSlot implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        String name = buf.readString();

        server.execute(() -> {
            BorderSlots slots = ComponentsRegistry.BORDER_SLOTS.get(player);

            for (BorderGroup slot : slots.slots) {
                if (
                    slot.name.toLowerCase().equals(name.toLowerCase())
                    ||
                    name.equals("+")
                ) {
                    player.sendMessage(new TranslatableText("gui.nationsmod.border_slot_creator_screen.name_not_unique"), false);
                    return;
                }
            }

            slots.slots.add(new BorderGroup(name));
        });
    }
    
}
