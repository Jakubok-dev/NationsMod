package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collections.Border;
import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.BorderSlots;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.collections.PlayerAccount;
import me.jakubok.nationsmod.collections.PlayerInfo;
import me.jakubok.nationsmod.networking.Packets;
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
            SEND_DATA_TO_CLIENT(slotName, player);
        });
    }
    

    public static void SEND_DATA_TO_CLIENT(String slotName, ServerPlayerEntity player) {
        BorderSlots slots = PlayerInfo.fromAccount(new PlayerAccount(player), player.server).slots;;

        for (BorderGroup slot : slots.slots) {
            if (slot.name.toLowerCase().equals(slotName.toLowerCase())) {

                if (slots.selectedSlot > -1)
                    for (Border block : slots.slots.get(slots.selectedSlot).toList()) {
                        PacketByteBuf buffer = PacketByteBufs.create();
                        buffer.writeBlockPos(block.position);
                        buffer.writeInt(Colour.GET_BITMASK(255, 255, 255));
                        buffer.writeBoolean(true);
                        ServerPlayNetworking.send(player, Packets.UNHIGHLIGHT_A_BLOCK_CLIENT, buffer);
                    }

                slots.selectedSlot = slots.slots.indexOf(slot);

                for (Border block : slot.toList()) {
                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeBlockPos(block.position);
                    buffer.writeInt(Colour.GET_BITMASK(255, 255, 255));
                    buffer.writeBoolean(true);
                    ServerPlayNetworking.send(player, Packets.HIGHLIGHT_A_BLOCK_CLIENT, buffer);
                }

                return;
            }
        }
    }
}
