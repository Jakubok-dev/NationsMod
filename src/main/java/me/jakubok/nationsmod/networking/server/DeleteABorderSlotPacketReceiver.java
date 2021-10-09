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
import net.minecraft.text.Text;

public class DeleteABorderSlotPacketReceiver implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        String slotName = buf.readString();

        server.execute(() -> {
            BorderSlots slots = ComponentsRegistry.BORDER_SLOTS.get(player);

            int slotIndex = -1;
            for (BorderGroup slot : slots.slots) {
                if (slot.name.toLowerCase().equals(slotName.toLowerCase())) {
                    slotIndex = slots.slots.indexOf(slot);
                    break;
                }
            }

            if (slotIndex == -1) {
                player.sendMessage(Text.of("This slot does not exist"), false);
                return;
            }

            if (slotIndex == slots.selectedSlot)
                slots.selectedSlot = -1;

            slots.slots.remove(slotIndex);
        });
    }
    
}
