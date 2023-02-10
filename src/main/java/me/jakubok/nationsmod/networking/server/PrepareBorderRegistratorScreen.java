package me.jakubok.nationsmod.networking.server;

import java.util.UUID;

import me.jakubok.nationsmod.collections.BorderSlots;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class PrepareBorderRegistratorScreen implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        UUID packetID = buf.readUuid();
        PacketByteBuf responseBuffer = PacketByteBufs.create();
        responseBuffer.writeUuid(packetID);

        BorderSlots borderSlots = ComponentsRegistry.BORDER_SLOTS.get(player);
        NbtCompound nbt = new NbtCompound();
        for (int i = 0; i < borderSlots.slots.size(); i++) {
            nbt.putString("name" + i, borderSlots.slots.get(i).name);
            nbt.putInt("index" + i, i);
        }
        nbt.putInt("size", borderSlots.slots.size());

        responseBuffer.writeNbt(nbt);
        ServerPlayNetworking.send(player, Packets.RECEIVE, responseBuffer);
    }
    
}
