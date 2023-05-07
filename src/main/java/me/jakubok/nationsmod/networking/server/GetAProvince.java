package me.jakubok.nationsmod.networking.server;

import java.util.UUID;

import me.jakubok.nationsmod.administration.province.Province;
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

public class GetAProvince implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbt = buf.readNbt();
        UUID packetID = buf.readUuid();
        UUID provinceId = nbt.getUuid("id");
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeUuid(packetID);
        server.execute(() -> {
            Province province = Province.fromUUID(provinceId, player.getEntityWorld().getLevelProperties());
            buffer.writeNbt(province.writeToNbtAndReturn(new NbtCompound()));
            ServerPlayNetworking.send(player, Packets.RECEIVE, buffer);
        });
    }
    
}
