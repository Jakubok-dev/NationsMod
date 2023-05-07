package me.jakubok.nationsmod.networking.server;

import java.util.UUID;

import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.BorderSlots;
import me.jakubok.nationsmod.collections.PlayerAccount;
import me.jakubok.nationsmod.collections.PlayerInfo;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class PrepareBorderSlotScreen implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        int index = buf.readInt();
        UUID messageID = buf.readUuid();
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeUuid(messageID);

        server.execute(() -> {
            if (index == -1) {
                ServerPlayNetworking.send(player, Packets.OPEN_BORDER_SLOT_CREATOR_SCREEN, PacketByteBufs.create());
                return;
            }

            BorderSlots slots = PlayerInfo.fromAccount(new PlayerAccount(player), server).slots;
            BorderGroup slot = slots.slots.get(index);

            NbtCompound compound = new NbtCompound();
            slot.writeToNbt(compound);
            buffer.writeNbt(compound);
            buffer.writeBoolean(slots.isSelected(slots.slots.indexOf(slot)));
            
            ServerPlayNetworking.send(player, Packets.RECEIVE, buffer);
        });
    }
    
}
